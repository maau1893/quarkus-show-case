FROM denoland/deno:1.29.0

ENV PORT=3000
ENV KAFKA_HOST=kafka:9094
ENV KAFKA_SSL=false

WORKDIR /app

COPY deps.ts .
COPY deno.json .
RUN deno cache --reload deps.ts

COPY main.ts .
COPY src/ src/


EXPOSE $PORT

CMD ["run", "--allow-net", "--allow-env", "main.ts"]
