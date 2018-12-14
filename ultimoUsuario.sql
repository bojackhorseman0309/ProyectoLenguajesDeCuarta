        CREATE TABLE usuarios
            (id_usuario         number not null,
            nombre_usuario      varchar2(20) not null,
            apellidos_usuario   varchar2(60) not null,
            correo_usuario      varchar2(40) unique not null,
            contrasena          varchar2 (30)not null,
                                CONSTRAINT usuarios_pk PRIMARY KEY (id_usuario));         
        
                
         
            CREATE TABLE rol
                (id_rol         number not null,
                nombre_rol      varchar2(20) unique not null,
                descripcion_rol varchar2(200),
                                CONSTRAINT rol_pk PRIMARY KEY (id_rol));   
 
 
         CREATE TABLE rol_usuario
                (id_usuario         number not null,
                id_rol              number not null,
                                    CONSTRAINT usuario_rol_pk PRIMARY KEY (id_usuario, id_rol),
                                    CONSTRAINT fk_usuariosrol FOREIGN KEY (id_usuario)
                                    REFERENCES usuarios(id_usuario),
                                    CONSTRAINT fk_rol FOREIGN KEY (id_rol)
                                    REFERENCES rol(id_rol));
                    
       CREATE TABLE proveedores 
            (id_proveedor           number not null,
            nombre_proveedor        varchar2(50) unique not null,
            telefono_proveedor      varchar2(10),
            correo_proveedor        varchar2(40),
                                    CONSTRAINT proveedores_pk PRIMARY KEY (id_proveedor));
                                    
        
            
         create table auditUsuario
            (id_usuario         number,
            nombre_usuario      varchar2(20),
            apellidos_usuario   varchar2(60),
            correo_usuario      varchar2(40),
            contrasena          varchar2 (30),
            fecha               date,
            operacion           varchar2(50),
            usuario             varchar2(50));    
            
             
             create table auditRol
                 (idRol        number,
                  nomRol       varchar2(100),
                  descrip      varchar2(100),
                  fecha        date,
                  operacion    varchar2(50),
                  usuario      varchar2(50)
                 );
             
             
             create table auditAsignaRol
                 (
                  idUsuario      number,
                  idRol          number,
                  fecha         date,
                  operacion     varchar2(50),
                  usuario       varchar2(50)
                 );
             
             
             CREATE TABLE auditProveedores
                 (
                 idProv         number,
                 nomProv        varchar2(100),
                 telProv        varchar2(100),
                 correoProv     varchar2(100),
                 fecha          date,
                 operacion      varchar2(50),
                 usuario        varchar2(50)
                 );
      
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
                                                      apelCli in varchar2, 
                                                      correoCli in varchar2,
                                                      conCli in varchar2)
                is
                    errorCor EXCEPTION;
                    conteo int;
                begin
                     select count(*) into conteo from usuarios where correo_usuario=correoCli;
                     if conteo>0 then
                        raise errorCor;
                     else
                     insert into usuarios 
                     values(seqClientes.nextval, nomCli, apelCli, correoCli, conCli);
                     end if;
                     
                EXCEPTION
                    WHEN errorCor THEN
                    RAISE_APPLICATION_ERROR(-20000, 'Ya hay un usuario con ese correo');
                end;
                
                
                create or replace procedure modificaClientes (idUsuario in number, nomCli in varchar2, 
                                                                apelCli in varchar2, correoCli in varchar2,
                                                                                        conCli in varchar2)
                    is
                        errorEx EXCEPTION;
                        conteo int;
                    begin
                    
                        select count(*) into conteo from usuarios where id_usuario=idUsuario;
                        if conteo>0 then
                        update usuarios
                        set nombre_usuario=nvl(nomCli, nombre_usuario),
                            apellidos_usuario=nvl(apelCli, apellidos_usuario),
                            correo_usuario=nvl(correoCli, correo_usuario),
                            contrasena=nvl(conCli, contrasena)
                        where id_usuario=idUsuario; 
                        else
                        raise errorEx;
                        end if;
                        
                        EXCEPTION
                        
                            when errorEx then
                                  RAISE_APPLICATION_ERROR(-20000, 'ID no encontrado');
                    end;         
            
            create or replace procedure borraCliente (idUsuario IN number)
                is
                  errorEx EXCEPTION;
                        conteo int;
                begin
                 select count(*) into conteo from usuarios where id_usuario=idUsuario;
                 if conteo>0 then
                    delete usuarios
                    where id_usuario=idUsuario;
                    else 
                    raise errorEx;
                    end if;
                    
                     EXCEPTION
                        
                            when errorEx then
                                  RAISE_APPLICATION_ERROR(-20000, 'ID no encontrado');
                end;
            
            
            create or replace trigger trBorraUsuario
                before delete on usuarios
                for each row
                begin 
                    delete from rol_usuario
                    where id_usuario=:old.id_usuario;  
                end;
            
            
            CREATE OR REPLACE PROCEDURE muestraTodoUsuarios(todoUsuario OUT SYS_REFCURSOR)
                AS             
                errorEx EXCEPTION;
                        conteo int;
                BEGIN
                select count(*) into conteo from usuarios;
                if conteo>0 then
                    OPEN todoUsuario FOR
                        SELECT * FROM usuarios
                        order by id_usuario;
                        else 
                        raise errorEx;
                        end if;
                       EXCEPTION
                        
                            when errorEx then
                                  RAISE_APPLICATION_ERROR(-20000, 'NO hay datos');
                END;
                
                CREATE OR REPLACE PROCEDURE muestraTodoUsuariosEspec(correo in varchar2          
                                                                     ,todoUsuarioEspec OUT SYS_REFCURSOR)
                    AS        
                      errorEx EXCEPTION;
                        conteo int;
                    BEGIN
                        select count(*) into conteo from usuarios where trim(lower(correo_usuario)) like trim(lower(correo))||'%';
                        if conteo>0 then
                        OPEN todoUsuarioEspec FOR
                            SELECT * FROM usuarios
                            where trim(lower(correo_usuario)) like trim(lower(correo))||'%';
                            
                            else
                            raise errorEx;
                            end if;
                            
                              EXCEPTION
                        
                            when errorEx then
                                  RAISE_APPLICATION_ERROR(-20000, 'Correo no encontrado');
                    END;
                    
                    


                   CREATE OR REPLACE PROCEDURE muestraTodoUsuariosEspecNom(nom in varchar2          
                                                                            ,todoUsuarioEspecNom OUT SYS_REFCURSOR)
                    AS        
                      errorEx EXCEPTION;
                        conteo int;
                    BEGIN
                    select count(*) into conteo from usuarios where trim(lower(nombre_usuario)) like trim(lower(nom))||'%';
                    if conteo>0 then
                        OPEN todoUsuarioEspecNom FOR
                            SELECT * FROM usuarios
                            where trim(lower(nombre_usuario)) like trim(lower(nom))||'%';
                            else
                            raise errorEx;
                            end if;
                            
                              EXCEPTION
                        
                            when errorEx then
                                  RAISE_APPLICATION_ERROR(-20000, 'Nombre no encontrado');
                    END;


                    CREATE OR REPLACE PROCEDURE muestraTodoUsuariosEspecApel(apellido in varchar2          
                                                                                ,todoUsuarioEspecApel OUT SYS_REFCURSOR)
                    AS      
                      errorEx EXCEPTION;
                        conteo int;
                    BEGIN
                       select count(*) into conteo from usuarios where trim(lower(apellidos_usuario)) like trim(lower(apellido))||'%';
                       if conteo>0 then
                        OPEN todoUsuarioEspecApel FOR
                            SELECT * FROM usuarios
                            where trim(lower(apellidos_usuario)) like trim(lower(apellido))||'%';
                            else 
                            raise errorEx;
                            end if;
                              EXCEPTION
                        
                            when errorEx then
                                  RAISE_APPLICATION_ERROR(-20000, 'Apellido no encontrado');
                    END;
                    

            
            
           
            
            
            create or replace function sacaId (correoUsuario in varchar2, idUsuario out int)
                return int
                as
                  errorEx EXCEPTION;
                        conteo int;
                begin
                 select count(*) into conteo from usuarios where LOWER(correo_usuario)=LOWER(correoUsuario);
                 if conteo>0 then
                    select id_usuario 
                    into idUsuario
                    from usuarios
                    where LOWER(correo_usuario)=LOWER(correoUsuario);
                return idUsuario;
                else
                raise errorEx;
                end if;
                
                  EXCEPTION
                        
                            when errorEx then
                                  RAISE_APPLICATION_ERROR(-20000, 'Correo no encontrado');
                end;
                
                create or replace procedure SpSacaId (correo in varchar2,idUsuario out int)
                    as
                      errorEx EXCEPTION;
                        conteo int;
                    begin                  
                      select count(*) into conteo from usuarios where correo_usuario=correo;
                      if conteo>0 then
                    idUsuario:=sacaId(correo, idUsuario);
                    else
                    raise errorEx;
                    end if;
                    
                      EXCEPTION
                        
                            when errorEx then
                                  RAISE_APPLICATION_ERROR(-20000, 'Correo no encontrado');
                    end;
                    
                    create or replace function verificaCorreo (correo in varchar2, msj out varchar2)
                    return varchar2
                        as
                        conteo int;
                        begin                       
                          select count(*) 
                          into conteo 
                          from usuarios 
                          where lower(correo_usuario)=trim(lower(correo));                       
                        if (conteo=0) then
                            msj:='false';
                        else
                            msj:='true';                    
                        end if;                  
                        return msj;
                        
                          EXCEPTION
                        
                            when others then
                                  RAISE_APPLICATION_ERROR(-20000, 'Error de funcion');
                        end;
            
            
            
            create or replace procedure spVerifCorreo (correo in varchar2, msj out varchar2)
            as

            begin
                msj:=verificaCorreo(correo, msj);     
                  EXCEPTION
                        
                            when others then
                                  RAISE_APPLICATION_ERROR(-20000, 'Error procedimiento');
            end;
            
                
            
            alter trigger trAuditUsuarios enable;
            
            create or replace trigger trAuditUsuarios
            before update or insert or delete on usuarios
            for each row
            begin
            
            if updating then
                insert into auditUsuario values (:old.ID_USUARIO, :old.nombre_usuario, :old.apellidos_usuario, :old.correo_usuario, :old.contrasena, sysdate,'UPDATE' ,user);
            
            elsif inserting then
                insert into auditUsuario values (:new.ID_USUARIO, :new.nombre_usuario, :new.apellidos_usuario, :new.correo_usuario, :new.contrasena, sysdate,'INSERT', user);
            else 
                 insert into auditUsuario values (:old.ID_USUARIO, :old.nombre_usuario, :old.apellidos_usuario, :old.correo_usuario, :old.contrasena, sysdate, 'DELETE',user);
            end if;
            
            end;
            
          

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
            errorEx EXCEPTION;
                        conteo int;
            begin
            if nombreRol is null then
                raise errorEx;
                else
              insert into rol values (seqRol.nextval, nombreRol, descripcionRol);
              end if;
              
              exception
                when errorEx then
                    raise_application_error(-20009, 'Nombre no puede ser vacio');
            end;
            

                
                
            create or replace procedure modificarRol (idRol in number, nombreRol in varchar2, descripcionRol in varchar2)
            
            is
                    errorEx EXCEPTION;
                        conteo int;     
            begin
                select count(*) into conteo from rol where id_rol=idRol;
                
                if conteo>0 then
            
                update rol
                set nombre_rol= nvl(nombreRol, nombre_rol),
                descripcion_rol=nvl(descripcionRol, descripcion_rol)
                where id_rol=idRol;
                
                else
                raise errorEx;
                end if;
                
                exception
                when errorEx then
                     raise_application_error(-20010, 'ID no coincide con registros');
            
            end;
            
            
            
            create or replace procedure borrarRol (idRol in number)
            
            is
            errorEx EXCEPTION;
                        conteo int;
            
            begin
             select count(*) into conteo from rol where id_rol=idRol;
                if conteo>0 then
                delete rol
                where id_rol=idRol;
                else
                raise errorEx;
                end if;
                  exception
                when errorEx then
                     raise_application_error(-20010, 'ID no coincide con registros');
            
            end;
            
            
             create or replace trigger trBorraRol
            
            before delete on rol
            
            for each row
            
            begin 
            
                delete from rol_usuario
                where id_rol=:old.id_rol;
            
            end;
            
                     
         CREATE OR REPLACE PROCEDURE muestraTodoRol(          
                    todoRol OUT SYS_REFCURSOR)
                    AS             
                BEGIN
                    OPEN todoRol FOR
                        SELECT * FROM rol;
                    exception 
                    
                    when NO_DATA_FOUND then
                        raise_application_error(-20011, 'Error en procedimiento');
                END;
                
                
                 CREATE OR REPLACE PROCEDURE muestraTodoRolEspec(nomRol in varchar2,         
                    todoRolEspec OUT SYS_REFCURSOR)
                    AS
                    errorEx EXCEPTION;
                        conteo int;
                BEGIN
                    select count(*) into conteo from rol where trim(upper(nombre_rol)) like trim(upper(nomRol))||'%';
                    if conteo>0 then
                    OPEN todoRolEspec FOR
                        SELECT * FROM rol
                        where trim(upper(nombre_rol)) like trim(upper(nomRol))||'%';
                        else 
                        raise errorEx;
                        end if;
                        
                        exception
                        when errorEx then
                            raise_application_error(-200012, 'Nombre invalido');
                END;
                
                
         create or replace function verificaNomRol (nomRol in varchar2, msj out varchar2)
            return varchar2
            as
            conteo int;
            begin
            
            select count(*) into conteo from rol where replace(lower(nombre_rol), ' ','')=replace(lower(nomRol), ' ','');
            
            if (conteo=0) then
            msj:='false';
            else
            msj:='true';
            
            end if;
            
            return msj;
            
            exception
                when others then
                raise_application_error(-200013,'Error de funcion');
            
            end;
            
            
            create or replace procedure spVerifNomRol (nomRol in varchar2, msj out varchar2)
            as
            begin
            msj:=verificaNomRol(nomRol, msj);
            exception
            when others then
            raise_application_error(-20014, 'Error de procedimiento');
            end;
            
           
            
             create or replace trigger trAuditRol
            before update or insert or delete on rol
            for each row
            begin
            
            if updating then
                insert into auditRol values (:old.ID_ROL, :old.NOMBRE_ROL, :old.DESCRIPCION_ROL, sysdate, 'UPDATE',  user);
            
            elsif inserting then
                   insert into auditRol values (:NEW.ID_ROL, :NEW.NOMBRE_ROL, :NEW.DESCRIPCION_ROL, sysdate, 'INSERT',  user);
            else 
                    insert into auditRol values (:old.ID_ROL, :old.NOMBRE_ROL, :old.DESCRIPCION_ROL, sysdate, 'DELETE', user);
            end if;
            
            end;
 
            --------------------------------ROL_USUARIO
            
            
            
            create or replace procedure asignaRol (idUsuario in number, idRol in number)
            
            as
              errorEx EXCEPTION;
            begin
                if idUsuario is null or idRol is null then
                raise errorEx;
                else
             insert into rol_usuario values (idUsuario, idRol);
             end if;
             
             exception
             
              when errorEx then
              raise_application_error(-20015,'Id de usuario o de rol esta vacio');
            
            end;
            

            create or replace procedure borraAsignaRol (idUsuario in number)
            
            as
              errorEx EXCEPTION;
                        conteo int;
            
            begin
                select count(*) into conteo from rol_usuario where id_usuario=idUsuario;
                
                if conteo>0 then
                delete rol_usuario
                where id_usuario=idUsuario;
                else
                raise errorEx;
                end if;
                
                exception
                when errorEx then
                raise_application_error(-20019, 'Id no coincide con registros');
            
            end;
            
            
            create or replace procedure modificaAsignaRol (idUsuario in number, idRol in number)
            
            as
              errorEx EXCEPTION;
                        conteo int;
            
            begin
            select count(*) into conteo from rol_usuario where id_usuario=idUsuario;
            
            if conteo>0 then
                
                update rol_usuario
                set id_rol=nvl(idRol, id_rol)
                where id_usuario=idUsuario;
                
                else
                raise errorEx;
                end if;
                
                exception
                 when errorEx then
                raise_application_error(-20019, 'Id no coincide con registros');
            
            end;
            
            
             CREATE OR REPLACE PROCEDURE muestraTodoRolAsig(todoRolAsig OUT SYS_REFCURSOR)
             AS             
                BEGIN
                    OPEN todoRolAsig FOR
                        SELECT * FROM rol_usuario;
                        
                        exception 
                        when no_data_found then
                        raise_application_error(-20020, 'No hay datos');
                END;
                
                CREATE OR REPLACE PROCEDURE muestraTodoRolAsigEspec(idUsuario in int, todoRolAsigEspec OUT SYS_REFCURSOR)
                    AS          
                      errorEx EXCEPTION;
                        conteo int;
                BEGIN
                select count(*) into conteo from rol_usuario where id_usuario=idUsuario;
                
                if conteo>0 then
                    OPEN todoRolAsigEspec FOR
                        SELECT * FROM rol_usuario
                        where id_usuario=idUsuario;
                        
                        else 
                        raise errorEx;
                        end if;
                        
                        exception 
                        when errorEx then
                         raise_application_error(-20021, 'No hay datos para ese id de usuario');
                END;
                
                
                
                
               create or replace trigger trAuditAsignaRol
            before update or insert or delete on rol_usuario
            for each row
            begin
            
            if updating then
                insert into auditAsignaRol values (:old.ID_USUARIO, :old.ID_ROL, SYSDATE, 'UPDATE',  user);
            
            elsif inserting then
                   insert into auditAsignaRol values (:NEW.ID_USUARIO, :NEW.ID_ROL, sysdate, 'INSERT',  user);
            else 
                    insert into auditAsignaRol values (:old.ID_USUARIO, :old.ID_ROL, sysdate, 'DELETE', user);
            end if;
            
            end;
            
            
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
            errorEx EXCEPTION;
                        conteo int;
            begin
            if nombreProveedor is null then
            raise errorEx;
            else
            
            insert into proveedores values (seqProveedores.nextval, nombreProveedor, telefonoProveedor, correoProveedor);
            end if;
            
            exception
            when erroEX then
            raise_application_error(-20021, 'Nombre proveedor vacio');
            
            end;
            
            execute insertaProveedores();
            
              create or replace procedure modificaProveedores (idProveedor in number, nombreProveedor in varchar2, 
                                                            telefonoProveedor in varchar2,  correoProveedor in varchar2 )
            as
            errorEx EXCEPTION;
                        conteo int;
            begin
            
            select count(*) into conteo from proveedores where id_proveedor=idProveedor;
            
            if conteo>0 then
            
            update proveedores
            set nombre_proveedor=nvl(nombreProveedor, nombre_proveedor),
            telefono_proveedor=nvl(telefonoProveedor, telefono_proveedor),
            correo_proveedor=nvl(correoProveedor, correo_proveedor)
            where id_proveedor=idProveedor;
            
            else
            raise errorEx;
            end if;
            
            exception 
            when errorEx then
            raise_application_error(-20022,'Id de proveedor no coincide con registros');
            
            end;
            
            
              create or replace procedure borraProveedores (idProveedor in number)
            as
            errorEx EXCEPTION;
                        conteo int;
            begin
            
               select count(*) into conteo from proveedores where id_proveedor=idProveedor;
            
            if conteo>0 then
            
            delete from proveedores
            where id_proveedor=idProveedor;
            
               else
            raise errorEx;
            end if;
            
               exception 
            when errorEx then
            raise_application_error(-20022,'Id de proveedor no coincide con registros');
            
            
            
            end;
            
            
            
            CREATE OR REPLACE PROCEDURE muestraTodoProv(todoProv OUT SYS_REFCURSOR)
                AS      
                BEGIN
                    OPEN todoProv FOR
                        SELECT * FROM proveedores;
                        exception
                        when no_data_found then
                        raise_application_error(-20023, 'No hay datos');
                END;
                
                
            CREATE OR REPLACE PROCEDURE muestraTodoProvEspec(nomProv in varchar2, todoProvEspec OUT SYS_REFCURSOR)
                AS     
                errorEx EXCEPTION;
                        conteo int;
                BEGIN
                select count(*) into conteo from proveedores where trim(lower(nombre_proveedor)) like trim(lower(nomProv))||'%';
                
                if conteo>0 then
                    OPEN todoProvEspec FOR
                        SELECT * FROM proveedores
                        where trim(lower(nombre_proveedor)) like trim(lower(nomProv))||'%';
                        
                        else 
                        raise errorEx;
                        end if;
                        
                        exception 
                        when errorEx then
                        raise_application_error(-20024, 'Nombre no coincide');
                END;
                
               
         create or replace function verificaNomProv (nomProv in varchar2, msj out varchar2)
            return varchar2
            as
            conteo int;
            begin
            
            select count(*) into conteo from proveedores where replace(lower(nombre_proveedor), ' ','')=replace(lower(nomProv), ' ','');
            
            if (conteo=0) then
                msj:='false';
            else
                msj:='true';
            
            end if;
            
            return msj;
            
            
            end;

            
            create or replace procedure spVerifNomProv (nomProv in varchar2, msj out varchar2)
            as

            begin

                 msj:=verificaNomProv(nomProv, msj);

                exception 
                when others then
                raise_application_error(-20025, 'Error de procedimiento');
            end;
            
        
            
            create or replace trigger trAuditProv
            before update or insert or delete on proveedores
            for each row
            begin
            
            if updating then
                insert into auditProveedores values (:old.ID_PROVEEDOR, :old.NOMBRE_PROVEEDOR, :OLD.TELEFONO_PROVEEDOR, :OLD.CORREO_PROVEEDOR, SYSDATE, 'UPDATE',  user);
            
            elsif inserting then
                   insert into auditProveedores values (:NEW.ID_PROVEEDOR, :NEW.NOMBRE_PROVEEDOR, :NEW.TELEFONO_PROVEEDOR,:NEW.CORREO_PROVEEDOR,sysdate, 'INSERT',  user);
            else 
                    insert into auditProveedores values (:old.ID_PROVEEDOR, :old.NOMBRE_PROVEEDOR, :OLD.TELEFONO_PROVEEDOR,:OLD.CORREO_PROVEEDOR, sysdate, 'DELETE', user);
            end if;
            
            end;
            
            
                
                
            









    
    
    
    
