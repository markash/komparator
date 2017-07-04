![alt text](https://github.com/markash/komparator/raw/master/ui/src/main/resources/kt-logo.png "Komparator") 

### Problem Statement
Did you ever want to compare to two lists of data and did not want to spend the time and effort to workout how to do this in Excel or ask a developer to sort it out? 
Would it not be great if you could specify the format of the data and how to map the columns; then let the application do the rest?

### How to

![alt text](http://res.cloudinary.com/yellowfire/image/upload/v1499160515/compare-setup_brvm8i.png "Setup")

##### Step 1 - Define the master data, i.e. source data
First we need to specify the format of the data that will be used as the source or master data for the comparison. This data is usually the authoritative source or golden copy.

##### Step 2 - Define the data to compare to the master, i.e. target data
Next we need to do the same for the target data which we suspect has :- 
- More records than the source data

- Less records than the source data

- Mismatch data between the source and target

##### Step 3 - Define the mapping between the source and target data
Since the source and target data might differ in format and attribute names defined in steps 1 & 2, we need to define a mapping between the columns in each data set that we are interested in comparing. 

##### Step 4 - Perform the comparison
All the meta data is inplace now to perform the actual comparison.

![alt text](http://res.cloudinary.com/yellowfire/image/upload/v1499160659/compare-differences_lajw9t.png "Differences")

### How to for developers
##### Step 1 - Define the master data, i.e. source data

```java
DataRecordConfiguration sourceConfiguration =
    new DataRecordConfiguration("AdventureWorks Person (Tab)", DataRecordFileType.DELIMITED)
            .withDelimiter('\t')
            .withColumn("ID", String.class)
            .withColumn("TYPE", String.class)
            .withColumn("NAME_STYLE", String.class)
            .withColumn("TITLE", String.class)
            .withColumn("FIRST_NAME", String.class)
            .withColumn("MIDDLE_NAME", String.class)
            .withColumn("LAST_NAME", String.class)
            .withColumn("SUFFIX", String.class)
            .withColumn("EMAIL_PROMOTION", String.class)
            .withColumn("ADDITIONAL_CONTEXT_INFO", String.class)
            .withColumn("DEMOGRAPHICS_INFO", String.class)
            .withColumn("ROWGUID_ID", String.class)
            .withColumn("MODIFIED_DATE", String.class);
```
##### Step 2 - Define the data to compare to the master, i.e. target data

```java
DataRecordConfiguration targetConfiguration =
    new DataRecordConfiguration("AdventureWorks Person (Semicolon)", DataRecordFileType.DELIMITED)
            .withDelimiter(';')
            .withColumn("ID", String.class)
            .withColumn("TYPE", String.class)
            .withColumn("NAME_STYLE", String.class)
            .withColumn("TITLE", String.class)
            .withColumn("FIRST_NAME", String.class)
            .withColumn("MIDDLE_NAME", String.class)
            .withColumn("LAST_NAME", String.class)
            .withColumn("SUFFIX", String.class)
            .withColumn("EMAIL_PROMOTION", String.class)
            .withColumn("ADDITIONAL_CONTEXT_INFO", String.class)
            .withColumn("DEMOGRAPHICS_INFO", String.class)
            .withColumn("ROWGUID_ID", String.class)
            .withColumn("MODIFIED_DATE", String.class);
```

##### Step 3 - Define the mapping between the source and target data

```java
AttributeMapping attributeMapping = new AttributeMapping("AdventureWorks Person Mapping")
            .source(configuration)
            .mapTo(configuration); 
```

##### Step 4 - Perform the comparison

```java
DataRecordReader reader = new DataRecordReader();
DataRecordSet sourceRecordSet = reader.read(sourceConfiguration, new FileReader(sourceFile));
DataRecordSet targetRecordSet = reader.read(targetConfiguration, new FileReader(targetFile));
List<ResultRecord> results = new ComparisonService().compare(sourceRecordSet, targetRecordSet, attributeMapping);
```

#### Integration Pattern

![alt text](http://res.cloudinary.com/yellowfire/image/upload/v1499160835/integration_jdfxa0.svg "Diagram")

#### Libraries
Three**Sixty** Compare uses the [FlatPack](http://flatpack.sourceforge.net) library to read data from comma-separated and fixed length files. and together with either an exact attribute name mapping.  
