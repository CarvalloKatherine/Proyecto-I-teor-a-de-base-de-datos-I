CREATE TABLE Usuario (
    id_usuario              INTEGER         IDENTITY NOT NULL,
    clave                   VARCHAR(30),
    primer_nombre           VARCHAR(100),
    segundo_nombre          VARCHAR(100),
    primer_apellido         VARCHAR(100),
    segundo_apellido        VARCHAR(100),
    correo_electronico      VARCHAR(200)    UNIQUE,
    fecha_registro          DATE,
    salario_mensual_base    NUMERIC(15,2),
    estado                  BIT             DEFAULT 1,
    creado_por              VARCHAR(200),
    creado_en               TIMESTAMP       DEFAULT CURRENT TIMESTAMP,
    modificado_por          VARCHAR(200),
    modificado_en           TIMESTAMP,
    PRIMARY KEY (id_usuario)
);

CREATE TABLE Presupuesto (
    id_presupuesto          INTEGER         IDENTITY NOT NULL,
    id_usuario              INTEGER,
    nombre_descriptivo      VARCHAR(100),
    anio_inicio             INTEGER,
    mes_inicio              INTEGER,
    anio_fin                INTEGER,
    mes_fin                 INTEGER,
    total_ingresos          NUMERIC(15,2),
    total_gastos            NUMERIC(15,2),
    total_ahorro            NUMERIC(15,2),
    fecha_hora_creacion     TIMESTAMP,
    estado_presupuesto      VARCHAR(30),
    creado_por              VARCHAR(200),
    creado_en               TIMESTAMP       DEFAULT CURRENT TIMESTAMP,
    modificado_por          VARCHAR(200),
    modificado_en           TIMESTAMP,
    PRIMARY KEY (id_presupuesto),
    FOREIGN KEY (id_usuario) REFERENCES Usuario (id_usuario)
);

CREATE TABLE Categoria (
    id_categoria            INTEGER         IDENTITY NOT NULL,
    nombre_categoria        VARCHAR(30),
    descripcion_detallada   VARCHAR(300),
    tipo_categoria          VARCHAR(30),
    nombre_icono            VARCHAR(30),
    color_hex               VARCHAR(30),
    orden_presentacion      VARCHAR(30),
    creado_por              VARCHAR(200),
    creado_en               TIMESTAMP       DEFAULT CURRENT TIMESTAMP,
    modificado_por          VARCHAR(200),
    modificado_en           TIMESTAMP,
    PRIMARY KEY (id_categoria)
);

CREATE TABLE SubCategoria (
    id_subcategoria         INTEGER         IDENTITY NOT NULL,
    id_categoria            INTEGER,
    nombre_subcategoria     VARCHAR(30),
    descripcion_detallada_sub VARCHAR(300),
    estado_sub              BIT             DEFAULT 1,
    por_defecto             BIT             DEFAULT 0,
    creado_por              VARCHAR(200),
    creado_en               TIMESTAMP       DEFAULT CURRENT TIMESTAMP,
    modificado_por          VARCHAR(200),
    modificado_en           TIMESTAMP,
    PRIMARY KEY (id_subcategoria),
    FOREIGN KEY (id_categoria) REFERENCES Categoria (id_categoria)
);

CREATE TABLE presupuesto_detalle (
    id_presupuesto_detalle  INTEGER         IDENTITY NOT NULL,
    id_presupuesto          INTEGER,
    id_subcategoria         INTEGER,
    monto_mensual_asignado  NUMERIC(15,2),
    observaciones           VARCHAR(100),
    creado_por              VARCHAR(200),
    creado_en               TIMESTAMP       DEFAULT CURRENT TIMESTAMP,
    modificado_por          VARCHAR(200),
    modificado_en           TIMESTAMP,
    PRIMARY KEY (id_presupuesto_detalle),
    FOREIGN KEY (id_presupuesto) REFERENCES Presupuesto (id_presupuesto),
    FOREIGN KEY (id_subcategoria) REFERENCES SubCategoria (id_subcategoria)
);


CREATE TABLE obligacion_fija (
    id_obligacion           INTEGER         IDENTITY NOT NULL,
    id_subcategoria         INTEGER,
    nombre_obligacion       VARCHAR(50),
    descripcion             VARCHAR(100),
    monto_fijo_mensual      NUMERIC(15,2),
    dia                     INTEGER,
    vigente                 BIT             DEFAULT 1,
    fecha_inicio            DATE,
    fecha_fin               DATE,
    creado_por              VARCHAR(200),
    creado_en               TIMESTAMP       DEFAULT CURRENT TIMESTAMP,
    modificado_por          VARCHAR(200),
    modificado_en           TIMESTAMP,
    PRIMARY KEY (id_obligacion),
    FOREIGN KEY (id_subcategoria) REFERENCES SubCategoria (id_subcategoria)
);


CREATE TABLE transaccion (
    id_transaccion          INTEGER         IDENTITY NOT NULL,
    id_presupuesto_detalle  INTEGER,
    anio                    INTEGER,
    mes                     INTEGER,
    tipo_transaccion        VARCHAR(30),
    descripcion             VARCHAR(100),
    monto                   NUMERIC(15,2),
    fecha                   DATE,
    metodo_pago             VARCHAR(50),
    num_factura             INTEGER,
    observaciones           VARCHAR(100),
    fecha_hora_registro     TIMESTAMP,
    creado_por              VARCHAR(200),
    creado_en               TIMESTAMP       DEFAULT CURRENT TIMESTAMP,
    modificado_por          VARCHAR(200),
    modificado_en           TIMESTAMP,
    PRIMARY KEY (id_transaccion),
    FOREIGN KEY (id_presupuesto_detalle) REFERENCES presupuesto_detalle (id_presupuesto_detalle)
);


CREATE TABLE obligacion_transaccion (
    id_transaccion          INTEGER         NOT NULL,
    id_obligacion           INTEGER,
    PRIMARY KEY (id_transaccion),
    FOREIGN KEY (id_obligacion) REFERENCES obligacion_fija (id_obligacion),
    FOREIGN KEY (id_transaccion) REFERENCES transaccion (id_transaccion)
);