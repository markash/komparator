package io.threesixty.compare.metamodel;

public class MetaModelTests {

//    @Test
//    public void testRead() throws Exception {
//
//        UpdateableDataContext sourceDataContext = getContext(FileSupplier.forResource("/source-persons.csv"));
//        UpdateableDataContext targetDataContext = getContext(FileSupplier.forResource("/target-persons.csv"));
//        UpdateableDataContext differenceDataContext = getContext(new File(FileSupplier.forResource("/").getAbsolutePath() + "/difference-persons.csv"));
//
//        DataSet sourceDataSet = getDataSet(sourceDataContext);
//        DataSet targetDataSet = getDataSet(targetDataContext);
//
//        Table sourceTable = sourceDataContext.getDefaultSchema().getTables().get(0);
//        Table targetTable = targetDataContext.getDefaultSchema().getTables().get(0);
//
//        ColumnMapping[] mappings = new ColumnMapping[] {
//                new ColumnMapping(
//                        sourceTable.getColumnByName("id"),
//                        targetTable.getColumnByName("id"),
//                        true),
//                new ColumnMapping(
//                        sourceTable.getColumnByName("name"),
//                        targetTable.getColumnByName("name")),
//                new ColumnMapping(
//                        sourceTable.getColumnByName("age"),
//                        targetTable.getColumnByName("age"))
//        };
//
//        differenceDataContext.executeUpdate(callback -> {
//            if (differenceDataContext.getDefaultSchema().getTableCount() > 0) {
//                callback.dropTable(differenceDataContext.getDefaultSchema().getTable(0)).execute();
//            }
//
//            TableCreationBuilder builder = callback.createTable(differenceDataContext.getDefaultSchema(), "difference-persons1.csv");
//            Arrays.stream(mappings).forEach(m -> builder.withColumn(m.source.getName()));
//            builder.execute();
//        });
//
//        List<FilterItem> join = Arrays.stream(mappings).filter(m -> m.key).map(m -> new FilterItem(new SelectItem(m.source), OperatorType.EQUALS_TO, new SelectItem(m.target))).collect(Collectors.toList());
//
//        DataSet resultSet = MetaModelHelper.getLeftJoin(sourceDataSet, targetDataSet, join.toArray(new FilterItem[join.size()]));
//        while (resultSet.next()) {
//            Row row = resultSet.getRow();
//
//            InsertInto insert = new InsertInto(differenceDataContext.getDefaultSchema().getTable(0));
//            for(ColumnMapping mapping : mappings) {
//                insert.value(mapping.source.getName(), mapping.applyTo(row));
//            }
//
//            differenceDataContext.executeUpdate(insert);
//        }
//
//
//        System.out.println("resultSet = " + resultSet);
//    }
//
//    UpdateableDataContext getContext(final File file) {
//        Objects.requireNonNull(file);
//        return getContext(file.toURI());
//    }
//
//    UpdateableDataContext getContext(final URI uri) {
//        Objects.requireNonNull(uri);
//
//        final DataContextPropertiesImpl properties = new DataContextPropertiesImpl();
//        properties.put("type", "csv");
//        properties.put("resource", uri);
//        return (UpdateableDataContext) DataContextFactoryRegistryImpl.getDefaultInstance().createDataContext(properties);
//    }
//
//    DataSet getDataSet(final DataContext dataContext) {
//        Objects.requireNonNull(dataContext);
//        Query query = dataContext.query().from(dataContext.getDefaultSchema().getTable(0)).selectAll().toQuery();
//        return dataContext.executeQuery(query);
//    }
//
//    DataSet create(ColumnMapping[] mapping) {
//        List<SelectItem> selectItems = Arrays.stream(mapping).map(m -> new SelectItem(m.source)).collect(Collectors.toList());
//        return new InMemoryDataSet(new SimpleDataSetHeader(selectItems));
//    }
//
//    static class ColumnMapping implements BiFunction<ColumnMapping, Row, String> {
//        public Column source;
//        public Column target;
//        public boolean key;
//
//        public ColumnMapping(final Column source, final Column target) {
//            this(source, target, false);
//        }
//
//        public ColumnMapping(final Column source, final Column target, final boolean isKey) {
//            this.source = source;
//            this.target = target;
//            this.key = isKey;
//        }
//
//        public String applyTo(final Row row) {
//            return apply(this, row);
//        }
//
//
//        @Override
//        public String apply(final ColumnMapping mapping, final Row row) {
//            final Optional<String> source = asString(row.getValue(mapping.source));
//            final Optional<String> target = asString(row.getValue(mapping.target));
//
//            MarkupCommandVisitor visitor = new MarkupCommandVisitor();
//            new StringsComparator(source.orElse(""), target.orElse("")).getScript().visit(visitor);
//            return visitor.getMarkup();
//        }
//
//        Optional<String> asString(final Object object) {
//            if (object instanceof String) {
//                return Optional.of((String) object);
//            } else if (object != null) {
//                return Optional.of(object.toString());
//            } else {
//                return Optional.empty();
//            }
//        }
//    }
}
