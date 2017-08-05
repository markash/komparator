# Attribute

An attribute of a Data Record

## Acceptance Criteria

### [Attribute](- "creation")

| [split][][Description](- "c:example") | [Text Value][full]   | [Requested Type][type]  | [Resultant Value][clazz] | [Resultant Value][value] |
| ---------------                       | -------------        | ----------------------- | ----------------------   |---------------------- |
| Simple name                           | David Peterson       | String                  | java.lang.String         | David Peterson        |
| Double-barrelled name                 | Mike Cannon-Brookes  | String                  | java.lang.String         | Mike Cannon-Brookes   | 
| Long                                  | 1000                 | Long                    | java.lang.Long           | 1000                  |
| Double No Decimal                     | 1000                 | Double                  | java.lang.Double         | 1000.0                |
| Double With Decimal                   | 1000.1234            | Double                  | java.lang.Double         | 1000.1234             |
| Date                                  | 1974-05-31           | Date                    | java.time.LocalDate      | 1974-05-31            |

[split]: - "#result = create(#fullName, #type)"
[full]: - "#fullName"
[type]: - "#type"
[clazz]:  - "?=#result.v1"
[value]:  - "?=#result.v2"

