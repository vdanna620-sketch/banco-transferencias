# Prompts utilizados (Cursor)

## 1. Regla del proyecto (`.cursor/rules/springboot.mdc`)
```
Crea un archivo de reglas para cursor (mdc) llamado 'springboot' que siempre se aplique, con las buenas practicas de Java 25 en el desarrollo de un servicio en spring boot para que las aplique en el desarrollo del proyecto.
Ten en cuenta que los nombres de clases, métodos y atributos deben ser en ingles.
Cada método público debe tener su documentación Javadoc actualizada.
Para los DTO usamos java records.
Agrega la estructura actual del proyecto dentro de las reglas.
```

## 2. Configuración de la base de datos (`application.properties`)
```
Crea la configuración para la conexión a la base de datos postgresql de nombre 'banktransferdb' con el usuario 'postgres' y contraseña 'root'.
Habilita la actualización del esquema automático en hibernate, el dialecto JPA e imprime las sentencias sql de JPA en consola.
```

## 3. Entidades JPA
```
Crea dos entidades JPA para un sistema de transferencias bancarias.
Account: id (Long, PK autogenerada), accountNumber (String único), ownerName (String), balance (BigDecimal precisión 19,2), createdAt (LocalDateTime). Con métodos debit(amount) y credit(amount).
BankTransaction: id (Long, PK autogenerada), reference (String UUID único), sourceAccount y targetAccount (ManyToOne LAZY a Account), amount (BigDecimal), description (String, opcional), status (enum TransactionStatus: COMPLETED), createdAt (LocalDateTime).
Genera getters y constructores. Usa las anotaciones JPA correctas.
```

## 4. DTO con records
```
Usando @.cursor/rules/springboot.mdc crea los DTO como java records con validaciones Jakarta:
AccountResponse, TransferRequest(sourceAccountNumber, targetAccountNumber, amount >= 0.01 con máximo 2 decimales, description opcional), TransactionResponse (incluye nombres y números de ambas cuentas, referencia, estado y fecha) y ErrorResponse(timestamp, status, error, message, path).
Agrega un método estático from(...) para mapear desde la entidad.
```

## 5. Repositorios
```
Crea los repositorios Spring Data JPA para Account y BankTransaction.
AccountRepository: un método con bloqueo pesimista (PESSIMISTIC_WRITE) para buscar una cuenta por número y actualizar su saldo.
BankTransactionRepository: listar todo el histórico ordenado por fecha descendente y filtrar por cuenta (origen o destino). Usa join fetch para evitar el problema N+1.
```

## 6. Capa Service
```
Crea AccountService (listar cuentas) y TransferService.
TransferService.transfer debe, dentro de una transacción:
- rechazar si la cuenta origen y destino son la misma
- bloquear las dos cuentas en un orden fijo para evitar deadlocks
- lanzar AccountNotFoundException si alguna no existe
- lanzar InsufficientFundsException si el saldo es menor al monto
- debitar, acreditar y guardar la BankTransaction con una referencia UUID
También debe consultar el histórico (con filtro opcional por cuenta).
Crea las excepciones personalizadas y un GlobalExceptionHandler (@RestControllerAdvice) con 404, 422 y 400.
```

## 7. Capa Controller
```
Crea los controladores REST:
- GET /accounts → listar cuentas con su saldo
- POST /transfers → realizar una transferencia
- GET /transactions?accountNumber= → histórico de transacciones
Devuelve códigos HTTP adecuados (201 al crear, 404, 422, 400) y documenta con anotaciones de OpenAPI.
```

## 8. Filtro CORS y datos de ejemplo
```
Crea una clase Configuration para crear un filtro de CORS que se aplicará a todas las rutas (/**). Acepta peticiones del origen http://localhost:3000 y permite todos los métodos y cabeceras necesarios.
Crea un CommandLineRunner que cargue 3 cuentas de ejemplo si la tabla está vacía.
```

## 9. Pruebas unitarias
```
Crea pruebas unitarias con JUnit 5 y Mockito para TransferService: transferencia exitosa, misma cuenta, saldo insuficiente (los saldos no deben cambiar) y cuenta inexistente.
```

## 10. Frontend – proyecto y servicio
```
Usando @.cursor/rules/springboot.mdc como regla, crea un proyecto React con Vite (puerto 3000) y un servicio BankService con AXIOS que tenga métodos para: obtener cuentas, transferir y obtener el histórico (con filtro opcional por cuenta). Usa una variable de entorno VITE_API_URL para la URL del backend. Realiza las configuraciones adecuadas para que todo quede en funcionamiento.
```

## 11. Frontend – formulario de transferencia
```
Crea un formulario en ReactJS para hacer una transferencia con: cuenta origen (select), cuenta destino (select), monto y descripción opcional. Todos los campos excepto la descripción son obligatorios. Al enviar, llama al método transfer del servicio. Si ocurre un error muestra un componente tipo toast con el mensaje del backend. Cada componente debe tener su archivo .jsx y su archivo .css separados.
```

## 12. Frontend – código QR
```
Cuando la transferencia sea exitosa muestra un modal con el resultado (monto, referencia, origen, destino y fecha) y un código QR (librería qrcode.react) que codifique en JSON la referencia, cuenta origen, cuenta destino, monto, estado y fecha.
```

## 13. Frontend – histórico y saldos
```
Crea una tabla dinámica con el histórico de transacciones (fecha, origen, destino, monto, estado) con un filtro por cuenta y un botón "Ver QR" por fila que abra el mismo modal. Crea también una tabla con las cuentas y sus saldos. Refresca saldos e histórico después de cada transferencia.
```
