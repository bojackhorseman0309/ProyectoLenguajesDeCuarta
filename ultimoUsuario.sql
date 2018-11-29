        create table usuarios
        (id_usuario number not null,
        nombre_usuario varchar2(20) not null,
        apellidos_usuario varchar2(60) not null,
        correo_usuario varchar2(40) unique not null,
        contrasena varchar2 (30)not null,
        CONSTRAINT usuarios_pk PRIMARY KEY (id_usuario));         
        
                
         
            create table rol
            (id_rol number not null,
            nombre_rol varchar2(20) unique not null,
            descripcion_rol varchar2(200),
            CONSTRAINT rol_pk PRIMARY KEY (id_rol));   
 
 
         create table rol_usuario
                (id_usuario number not null,
                id_rol number not null,
                CONSTRAINT usuario_rol_pk PRIMARY KEY (id_usuario, id_rol),
                CONSTRAINT fk_usuariosrol FOREIGN KEY (id_usuario)
                    REFERENCES usuarios(id_usuario),
                CONSTRAINT fk_rol FOREIGN KEY (id_rol)
                    REFERENCES rol(id_rol));
                    
       create table proveedores 
        (id_proveedor number not null,
        nombre_proveedor varchar2(50) unique not null,
        telefono_proveedor varchar2(10),
        correo_proveedor varchar2(40),
        CONSTRAINT proveedores_pk PRIMARY KEY (id_proveedor));
        

                    
            insert into rol_usuario values (1,100);
            insert into rol_usuario values (2,101);
            insert into rol values(100, 'Administrador', 'Administrador');
            insert into rol values(101,'Usuario', 'Usuario');
            insert into usuarios values (1, 'Alonso', 'Araya', 'alonso@araya.com', 'alonso');
            insert into usuarios values (2, 'Bryan', 'Ruiz', 'bryan@ruiz.com', 'bryan');
            insert into proveedores values (1000, 'Oracle S.A', '1234-5678', 'oracle@oracle.com')
            
            
            select * from usuarios;
            select * from rol_usuario;
            select * from rol;
            select * from proveedores;
            set verify off;
            set serverOutput on;
            
            ------------------------------------------------Usuarios
            
            create sequence seqClientes
              INCREMENT BY 5
              START WITH 20
              MINVALUE 1
              MAXVALUE 4999
              NOCACHE
              NOCYCLE;
              
              drop sequence seqClientes;
                        
            create or replace procedure creaClientes (nomCli in varchar2, 
                                                      apelCli in varchar2, correoCli in varchar2,
                                                      conCli in varchar2)
                is
                begin
                
                insert into usuarios values(seqClientes.nextval, nomCli, apelCli, correoCli, conCli);
                
                end;
                
                execute creaClientes ('Paula', 'Calvo', 'correo@correo.com', 'cacaaa');
                
                create or replace procedure modificaClientes (idUsuario in number, nomCli in varchar2, apelCli in varchar2, correoCli in varchar2, conCli in varchar2)
                is
                
                begin
                
                    update usuarios
                    set nombre_usuario=nvl(nomCli, nombre_usuario),
                    apellidos_usuario=nvl(apelCli, apellidos_usuario),
                    correo_usuario=nvl(correoCli, correo_usuario),
                    contrasena=nvl(conCli, contrasena)
                    where id_usuario=idUsuario;
                    
                end;
            
            execute modificaClientes (1, 'Bombon', 'Araya', null, null);
            
            
            create or replace procedure borraCliente (idUsuario IN number)
            is
            
            begin
            
                delete usuarios
                where id_usuario=idUsuario;
            
            end;
            
            execute borraCliente (2);
            
            create or replace trigger trBorraUsuario
            
            before delete on usuarios
            
            for each row
            
            begin 
            
                delete from rol_usuario
                where id_usuario=:old.id_usuario;
            
            end;
            
            
            CREATE OR REPLACE PROCEDURE muestraTodoUsuarios(          
                    todoUsuario OUT SYS_REFCURSOR)AS             
                BEGIN
                    OPEN todoUsuario FOR
                        SELECT * FROM usuarios
                        order by id_usuario;
                END;
                
                CREATE OR REPLACE PROCEDURE muestraTodoUsuariosEspec(correo in varchar2          
                                                                     ,todoUsuarioEspec OUT SYS_REFCURSOR)
                    AS             
                    BEGIN
                        OPEN todoUsuarioEspec FOR
                            SELECT * FROM usuarios
                            where correo_usuario=correo;
                    END;
                   
            
            
           
            
            
            create or replace function sacaId (correoUsuario in varchar2, idUsuario out int)
                return int
                
                as
                
                begin
                
                select id_usuario into idUsuario
                from usuarios
                where correo_usuario=correoUsuario;
                
                return idUsuario;
                
                end;
                
                create or replace procedure SpSacaId (correo in varchar2,idUsuario out int)
                    as
                    
                    begin
                    
                    idUsuario:=sacaId(correo, idUsuario);

                    end;
                    
                    declare
                    aux int;
                    begin
                    SpSacaId('bryan@ruiz.com', aux);
                    end;
                    
                select * from usuarios;
                
                set verify off;
                set serveroutput on;
            
            
    
    --------------------------------------------ROL
            
            create sequence seqRol
              INCREMENT BY 5
              START WITH 5000
              MINVALUE 5000
              MAXVALUE 6000
              NOCACHE
              NOCYCLE;
              
              drop sequence seqRol;
    
    
            create or replace procedure crearRol ( nombreRol in varchar2, descripcionRol in varchar2)
            
            is
            
            begin
            
              insert into rol values (seqRol.nextval, nombreRol, descripcionRol);
            
            end;
            
            execute crearRol ( 'Vago', 'Vagazo');
                
                select * from rol;
                
                
            create or replace procedure modificarRol (idRol in number, nombreRol in varchar2, descripcionRol in varchar2)
            
            is
            
            begin
            
                update rol
                set nombre_rol= nvl(nombreRol, nombre_rol),
                descripcion_rol=nvl(descripcionRol, descripcion_rol)
                where id_rol=idRol;
            
            end;
            
            execute modificarRol (100, 'Caca', null);
            
            
            create or replace procedure borrarRol (idRol in number)
            
            is
            
            begin
                
                delete rol
                where id_rol=idRol;
            
            end;
            
            execute borrarRol (1);
            
             create or replace trigger trBorraRol
            
            before delete on rol
            
            for each row
            
            begin 
            
                delete from rol_usuario
                where id_rol=:old.id_rol;
            
            end;
            
            
            select * from rol;
            
         CREATE OR REPLACE PROCEDURE muestraTodoRol(          
                    todoRol OUT SYS_REFCURSOR)AS             
                BEGIN
                    OPEN todoRol FOR
                        SELECT * FROM rol;
                END;
                
                
                 CREATE OR REPLACE PROCEDURE muestraTodoRolEspec(nomRol in varchar2,         
                    todoRolEspec OUT SYS_REFCURSOR)AS             
                BEGIN
                    OPEN todoRolEspec FOR
                        SELECT * FROM rol
                        where upper(nombre_rol)=upper(nomRol);
                END;
                
                select * from rol;
            
            --------------------------------ROL_USUARIO
            
            
            
            create or replace procedure asignaRol (idUsuario in number, idRol in number)
            
            as
            
            begin
            
             insert into rol_usuario values (idUsuario, idRol);
            
            end;
            
            execute asignaRol(1, 2);
            
            create or replace procedure borraAsignaRol (idUsuario in number)
            
            as
            
            begin
            
                delete rol_usuario
                where id_usuario=idUsuario;
            
            end;
            
            execute borraASignaRol(1);
            
            create or replace procedure modificaAsignaRol (idUsuario in number, idRol in number)
            
            as
            
            begin
                
                update rol_usuario
                set id_rol=nvl(idRol, id_rol)
                where id_usuario=idUsuario;
            
            end;
            
            execute modificaAsignaRol(1, 1);
            
            select id_usuario, id_rol from rol_usuario;
            select * from rol;
            
             CREATE OR REPLACE PROCEDURE muestraTodoRolAsig(todoRolAsig OUT SYS_REFCURSOR)
             AS             
                BEGIN
                    OPEN todoRolAsig FOR
                        SELECT * FROM rol_usuario;
                END;
                
                CREATE OR REPLACE PROCEDURE muestraTodoRolAsigEspec(idUsuario in int, todoRolAsigEspec OUT SYS_REFCURSOR)
                    AS             
                BEGIN
                    OPEN todoRolAsigEspec FOR
                        SELECT * FROM rol_usuario
                        where id_usuario=idUsuario;
                END;
            
            -----------------------------------Proveedores
            
            select * from proveedores;
                
                create sequence seqProveedores
              INCREMENT BY 5
              START WITH 6001
              MINVALUE 6001
              MAXVALUE 10000
              NOCACHE
              NOCYCLE;
            
            create or replace procedure insertaProveedores ( nombreProveedor in varchar2, 
                                                            telefonoProveedor in varchar2,  correoProveedor in varchar2 )
            as
            begin
            
            insert into proveedores values (seqProveedores.nextval, nombreProveedor, telefonoProveedor, correoProveedor);
            
            end;
            
            execute insertaProveedores();
            
              create or replace procedure modificaProveedores (idProveedor in number, nombreProveedor in varchar2, 
                                                            telefonoProveedor in varchar2,  correoProveedor in varchar2 )
            as
            begin
            
            update proveedores
            set nombre_proveedor=nvl(nombreProveedor, nombre_proveedor),
            telefono_proveedor=nvl(telefonoProveedor, telefono_proveedor),
            correo_proveedor=nvl(correoProveedor, correo_provedor)
            where id_proveedor=idProveedor;
            
            end;
            
            
            execute modificaProveedores();
            
              create or replace procedure borraProveedores (idProveedor in number)
            as
            begin
            
            delete from proveedores
            where id_proveedor=idProveedor;
            
            end;
            
            
            CREATE OR REPLACE PROCEDURE muestraTodoProv(todoProv OUT SYS_REFCURSOR)
                AS             
                BEGIN
                    OPEN todoProv FOR
                        SELECT * FROM proveedores;
                END;
                
                
            CREATE OR REPLACE PROCEDURE muestraTodoProvEspec(idProv in int, todoProvEspec OUT SYS_REFCURSOR)
                AS             
                BEGIN
                    OPEN todoProvEspec FOR
                        SELECT * FROM proveedores
                        where id_proveedor=idProv;
                END;
                
                
            









    
    
    
    
