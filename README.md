# Calendar service

Develop a flat viewing scheduler.

There are 2 parties to the service:
- New Tenants who are moving in
- Landlord who shows the flat

Scenario 1:
Flat has 20-minute viewing slots from 10:00 to 20:00 for the upcoming 7 days

- New Tenant should be able to reserve viewing. Once reserved slot can not be occupied by other tenants.
- Landlord should be notified about reservation in at least 24 hours and can either approve or reject it. Once rejected this slot can not be reserved by anyone else at any point.
- New Tenant gets notified about approval or rejection.
- New Tenant can cancel the viewing at any point, landlord should be notified and viewing slot becomes vacant.

Constrains:
- No DB
- No Auth
- Http server
- No CRUD

## Time tracker:
| Day | Time | Description |
| :--- | :---: | :----------- |
| Saturday | 18:00 - 20:00 | Setup project, read documentation |
| Sunday | 17:00 - 18:00 | Fix issue with injection |
|  |  |  |


## Feature http-client documentation

- [Micronaut Micronaut HTTP Client documentation](https://docs.micronaut.io/latest/guide/index.html#httpClient)

