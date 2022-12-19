FROM denoland/deno:1.29.0

ENV PORT=3000
ENV KAFKA_HOST=kafka:9094
ENV KAFKA_SSL=false

WORKDIR /app

COPY import_map.json .
COPY deno.json .
COPY main.ts .
COPY src/ src/

RUN deno cache main.ts

EXPOSE $PORT

CMD ["run", "--allow-net", "--allow-env", "main.ts"]
