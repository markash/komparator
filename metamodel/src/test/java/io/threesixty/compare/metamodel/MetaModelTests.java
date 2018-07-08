package io.threesixty.compare.metamodel;

import io.threesixty.compare.markup.MarkupCommandVisitor;
import io.threesixty.compare.util.FileSupplier;
import org.apache.commons.text.diff.StringsComparator;
import org.apache.metamodel.*;
import org.apache.metamodel.create.TableCreationBuilder;
import org.apache.metamodel.data.DataSet;
import org.apache.metamodel.data.InMemoryDataSet;
import org.apache.metamodel.data.Row;
import org.apache.metamodel.data.SimpleDataSetHeader;
import org.apache.metamodel.factory.DataContextFactoryRegistryImpl;
import org.apache.metamodel.factory.DataContextPropertiesImpl;
import org.apache.metamodel.insert.InsertInto;
import org.apache.metamodel.query.FilterItem;
import org.apache.metamodel.query.OperatorType;
import org.apache.metamodel.query.Query;
import org.apache.metamodel.query.SelectItem;
import org.apache.metamodel.schema.Column;
import org.junit.Test;

import java.io.File;
import java.net.URI;
import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public class MetaModelTests {

    @Test
    public void testRead() throws Exception {

        UpdateableDataContext sourceDataContext = getContext(FileSupplier.forResource("/source-persons.csv"));
        UpdateableDataContext targetDataContext = getContext(FileSupplier.forResource("/target-persons.csv"));
        UpdateableDataContext differenceDataContext = getContext(new File(FileSupplier.forResource("/").getAbsolutePath() + "/difference-persons.csv"));

        DataSet sourceDataSet = getDataSet(sourceDataContext);
        DataSet targetDataSet = getDataSet(targetDataContext);

        ColumnMapping[] mappings = new ColumnMapping[] {
                new ColumnMapping(
                        sourceDataContext.getDefaultSchema().getTables()[0].getColumnByName("id"),
                        targetDataContext.getDefaultSchema().getTables()[0].getColumnByName("id"),
                        true),
                new ColumnMapping(
                        sourceDataContext.getDefaultSchema().getTables()[0].getColumnByName("name"),
                        targetDataContext.getDefaultSchema().getTables()[0].getColumnByName("name")),
                new ColumnMapping(
                        sourceDataContext.getDefaultSchema().getTables()[0].getColumnByName("age"),
                        targetDataContext.getDefaultSchema().getTables()[0].getColumnByName("age"))
        };

        differenceDataContext.executeUpdate(new UpdateScript() {
            @Override
            public void run(UpdateCallback callback) {
                if (differenceDataContext.getDefaultSchema().getTableCount() > 0) {
                    callback.dropTable(differenceDataContext.getDefaultSchema().getTable(0)).execute();
                }

                TableCreationBuilder builder = callback.createTable(differenceDataContext.getDefaultSchema(), "difference-persons1.csv");
                Arrays.stream(mappings).forEach(m -> builder.withColumn(m.source.getName()));
                builder.execute();
            }
        });

        List<FilterItem> join = Arrays.stream(mappings).filter(m -> m.key).map(m -> new FilterItem(new SelectItem(m.source), OperatorType.EQUALS_TO, new SelectItem(m.target))).collect(Collectors.toList());

        DataSet resultSet = MetaModelHelper.getLeftJoin(sourceDataSet, targetDataSet, join.toArray(new FilterItem[join.size()]));
        while (resultSet.next()) {
            Row row = resultSet.getRow();

            InsertInto insert = new InsertInto(differenceDataContext.getDefaultSchema().getTable(0));
            for(ColumnMapping mapping : mappings) {
                insert.value(mapping.source.getName(), mapping.applyTo(row));
            }

            differenceDataContext.executeUpdate(insert);
        }


        System.out.println("resultSet = " + resultSet);
    }

    UpdateableDataContext getContext(final File file) {
        Objects.requireNonNull(file);
        return getContext(file.toURI());
    }

    UpdateableDataContext getContext(final URI uri) {
        Objects.requireNonNull(uri);

        final DataContextPropertiesImpl properties = new DataContextPropertiesImpl();
        properties.put("type", "csv");
        properties.put("resource", uri);
        return (UpdateableDataContext) DataContextFactoryRegistryImpl.getDefaultInstance().createDataContext(properties);
    }

    DataSet getDataSet(final DataContext dataContext) {
        Objects.requireNonNull(dataContext);
        Query query = dataContext.query().from(dataContext.getDefaultSchema().getTable(0)).selectAll().toQuery();
        return dataContext.executeQuery(query);
    }

    DataSet create(ColumnMapping[] mapping) {
        List<Column> selectItems = Arrays.stream(mapping).map(m -> m.source).collect(Collectors.toList());
        return new InMemoryDataSet(new SimpleDataSetHeader(selectItems.toArray(new Column[selectItems.size()])), new ArrayList<>());
    }

    static class ColumnMapping implements BiFunction<ColumnMapping, Row, String> {
        public Column source;
        public Column target;
        public boolean key;

        public ColumnMapping(final Column source, final Column target) {
            this(source, target, false);
        }

        public ColumnMapping(final Column source, final Column target, final boolean isKey) {
            this.source = source;
            this.target = target;
            this.key = isKey;
        }

        public String applyTo(final Row row) {
            return apply(this, row);
        }


        @Override
        public String apply(final ColumnMapping mapping, final Row row) {
            final Optional<String> source = asString(row.getValue(mapping.source));
            final Optional<String> target = asString(row.getValue(mapping.target));

            MarkupCommandVisitor visitor = new MarkupCommandVisitor();
            new StringsComparator(source.orElse(""), target.orElse("")).getScript().visit(visitor);
            return visitor.getMarkup();
        }

        Optional<String> asString(final Object object) {
            if (object instanceof String) {
                return Optional.of((String) object);
            } else if (object != null) {
                return Optional.of(object.toString());
            } else {
                return Optional.empty();
            }
        }
    }
}
