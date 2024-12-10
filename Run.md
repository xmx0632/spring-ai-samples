## doc

    https://springdoc.cn/spring-ai-rag-using-embedding-models-vector-databases/



## 修改点

    使用 jdk17 
    在 mac m1 运行，修改 postgres 配置
    

    ```
      postgres:
    image: 'arm64v8/postgres'
    environment:
      - 'POSTGRES_DB=postgres'
      - 'POSTGRES_PASSWORD=postgres'
      - 'POSTGRES_USER=postgres'
    volumes:
      - './data:/var/lib/postgresql/data'
    ports:
      - '5432:5432'
    ```

    新增配置application.yml，加入配置项
    ```
    spring:
      datasource:
        url: jdbc:postgresql://localhost:5432/postgres
        username: postgres
        password: postgres
        driver-class-name: org.postgresql.Driver

      flyway:
        enabled: false
        locations: classpath:db/migration
        baseline-on-migrate: true
        baseline-version: 1
    ```

使用 spring.ai.ollama.chat.options.model=llama3.2:latest

## 启动 VectorStoreConfig.java

  需要拉取大模型
    
    ollama pull mxbai-embed-large

  