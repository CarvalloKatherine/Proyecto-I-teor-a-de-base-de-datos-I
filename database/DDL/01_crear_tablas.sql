CREATE TABLE Usuario (
    id_usuario              VARCHAR(30)     NOT NULL,
    clave                   VARCHAR(30),
    primer_nombre           VARCHAR(100),
    segundo_nombre          VARCHAR(310),
    primer_apellido         VARCHAR(100),
    segundo_apellido        VARCHAR(100),
    correo_electronico      VARCHAR(200)    UNIQUE,
    fecha_registro          DATE,
    salario_mensual_base    DOUBLE,
    estado                  BIT,
    creado_por              VARCHAR(200),
    creado_en               TIMESTAMP,
    modificado_por          VARCHAR(200),
    modificado_en           TIMESTAMP,
    PRIMARY KEY (id_usuario)
);

CREATE TABLE Presupuesto (
    id_presupuesto          VARCHAR(30)     NOT NULL,
    id_usuario              VARCHAR(30),
    usuario                 VARCHAR(200),
    nombre_descriptivo      VARCHAR(100),
    anio_inicio             INTEGER,
    mes_inicio              INTEGER,
    anio_fin                INTEGER,
    mes_fin                 INTEGER,
    total_ingresos          DOUBLE,
    total_gastos            DOUBLE,
    total_ahorro            DOUBLE,
    fecha_hora_creacion     TIMESTAMP,
    estado_presupuesto      VARCHAR(30),
    creado_por              VARCHAR(200),
    creado_en               TIMESTAMP,
    modificado_por          VARCHAR(200),
    modificado_en           TIMESTAMP,
    PRIMARY KEY (id_presupuesto),
    FOREIGN KEY (id_usuario) REFERENCES Usuario (id_usuario)
);


CREATE TABLE Categoria (
    id_categoria            VARCHAR(30)     NOT NULL,
    nombre_categoria        VARCHAR(30),
    descripcion_detallada   VARCHAR(300),
    tipo_categoria          VARCHAR(30),
    nombre_icono            VARCHAR(30),
    color_hex               VARCHAR(30),
    orden_presentacion      VARCHAR(30),
    creado_por              VARCHAR(200),
    creado_en               TIMESTAMP,
    modificado_por          VARCHAR(200),
    modificado_en           TIMESTAMP,
    PRIMARY KEY (id_categoria)
);


CREATE TABLE SubCategoria (
    id_subcategoria         VARCHAR(30)     NOT NULL,
    id_categoria            VARCHAR(30),
    categoria_padre         VARCHAR(30),
    nombre_subcategoria     VARCHAR(30),
    descripcion_detallada_sub VARCHAR(300),
    estado_sub              BIT,
    por_defecto             BIT,
    creado_por              VARCHAR(200),
    creado_en               TIMESTAMP,
    modificado_por          VARCHAR(200),
    modificado_en           TIMESTAMP,
    PRIMARY KEY (id_subcategoria),
    FOREIGN KEY (id_categoria) REFERENCES Categoria (id_categoria)
);


CREATE TABLE presupuesto_detalle (
    id_presupuesto_detalle  VARCHAR(30)     NOT NULL,
    id_presupuesto          VARCHAR(30),
    id_subcategoria         VARCHAR(30),
    presupuesto_padre       VARCHAR(30),
    subcategoria            VARCHAR(30),
    monto_mensual_asignado  DOUBLE,
    observaciones           VARCHAR(100),
    creado_por              VARCHAR(200),
    creado_en               TIMESTAMP,
    modificado_por          VARCHAR(200),
    modificado_en           TIMESTAMP,
    PRIMARY KEY (id_presupuesto_detalle),
    FOREIGN KEY (id_presupuesto) REFERENCES Presupuesto (id_presupuesto),
    FOREIGN KEY (id_subcategoria) REFERENCES SubCategoria (id_subcategoria)
);


CREATE TABLE obligacion_fija (
    id_obligacion           VARCHAR(30)     NOT NULL,
    id_subcategoria         VARCHAR(30),
    usuario                 VARCHAR(30),
    subcategoria            VARCHAR(30),
    nombre_obligacion       VARCHAR(50),
    descripcion             VARCHAR(100),
    monto_fijo_mensual      DOUBLE,
    dia                     INTEGER,
    vigente                 BIT,
    fecha_inicio            DATE,
    fecha_fin               DATE,
    creado_por              VARCHAR(200),
    creado_en               TIMESTAMP,
    modificado_por          VARCHAR(200),
    modificado_en           TIMESTAMP,
    PRIMARY KEY (id_obligacion),
    FOREIGN KEY (id_subcategoria) REFERENCES SubCategoria (id_subcategoria)
);


CREATE TABLE transaccion (
    id_transaccion          VARCHAR(30)     NOT NULL,
    id_presupuesto_detalle  VARCHAR(30),
    usuario                 VARCHAR(30),
    presupuesto             VARCHAR(50),
    anio                    INTEGER,
    mes                     INTEGER,
    subcategoria            VARCHAR(30),
    obligacion              VARCHAR(30),
    tipo_transaccion        VARCHAR(30),
    descripcion             VARCHAR(100),
    monto                   DOUBLE,
    fecha                   DATE,
    metodo_pago             VARCHAR(50),
    num_factura             INTEGER,
    observaciones           VARCHAR(100),
    fecha_hora_registro     TIMESTAMP,
    creado_por              VARCHAR(200),
    creado_en               TIMESTAMP,
    modificado_por          VARCHAR(200),
    modificado_en           TIMESTAMP,
    PRIMARY KEY (id_transaccion),
    FOREIGN KEY (id_presupuesto_detalle) REFERENCES presupuesto_detalle (id_presupuesto_detalle)
);


CREATE TABLE obligacion_transaccion (
    id_transaccion          VARCHAR(30)     NOT NULL,
    id_obligacion           VARCHAR(30),
    PRIMARY KEY (id_transaccion, id_obligacion),
    FOREIGN KEY (id_obligacion) REFERENCES obligacion_fija (id_obligacion),
    FOREIGN KEY (id_transaccion) REFERENCES transaccion (id_transaccion)
);