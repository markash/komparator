# Comparison

Compare two lists for attribute differences and missing records

## Acceptance Criteria

- A record is deemed EQUAL when the id column matches and the mapped attributes match are equal.
- A record is deemed MISMATCHED when the id column matches but one or more mapped attributes are not equal.
- A record is deemed SOURCE_UNMATCHED when the id column value is not found in the target list.
- A record is deemed TARGET_UNMATCHED when the id column value is not found in the source list.

### [Lists with a single long id attribute](- "basic")

Give a file of people [source-persons.csv](- "#source") and another file of people [target-persons.csv](- "#target")

When the system compares the lists

Then the following is found:

| [ ][compare] [ID][id]| [Type][type]       | [Name][name]  | [Age][age]
|----------------------|--------------------|---------------|-----------|
| 1                    | MISMATCH           | Mark (Marcus) | 42 (43)   |
| 2                    | MISMATCH           | Natasja       | 41 (42)   |
| 3                    | SOURCE_UNMATCHED   | Dillon        | 16        |
| 4                    | TARGET_UNMATCHED   | Crystal       | 15        |
| 5                    | TARGET_UNMATCHED   | Katie         | 5         |
| 6                    | EQUAL              | Shadow        | 5         |

[compare]: - "c:verify-rows=#result:compare(#source,#target)"
[id]: - "?=#result['ID']"
[type]: - "?=#result['resultType']"
[name]: - "?=#result['FIRSTNAME']"
[age]: - "?=#result['AGE']"


