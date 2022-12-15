# Dummy Server

Dummy server to provide some REST endpoints and interact with Kafka for testing
purposes of the quarkus-showcase application.

## Development Setup

Install deno from [deno.land](https://deno.land)

```bash
# load cache dependencies
deno cache --reload --lock=deno.lock main.ts

# run application
deno run --allow-net --allow-env main.ts
```

When adding new dependencies to the `import_map.json` run following command to
update the lock file

```bash
deno cache --lock=deno.lock --lock-write main.ts
```
