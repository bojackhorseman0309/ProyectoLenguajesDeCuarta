create table usuarios
        (id_usuario number not null,
        nombre_usuario varchar2(30) not null,
        apellidos_usuario varchar2(60) not null,
        correo_usuario varchar2(40) not null,
        contrasena varchar2 (30)not null,
        CONSTRAINT usuarios_pk PRIMARY KEY (id_usuario));         
        
 
 
 create table rol
        (id_rol number not null,
        nombre_rol varchar2(20) not null,
        descripcion_rol varchar2(60),
        CONSTRAINT rol_pk PRIMARY KEY (id_rol));   
 
 
 create table rol_usuario
        (id_usuario number not null,
        id_rol number not null,
        CONSTRAINT usuario_rol_pk PRIMARY KEY (id_usuario, id_rol),
        CONSTRAINT fk_usuariosrol FOREIGN KEY (id_usuario)
            REFERENCES usuarios(id_usuario),
        CONSTRAINT fk_rol FOREIGN KEY (id_rol)
            REFERENCES rol(id_rol));
            
    insert into rol_usuario values (1,2);
    insert into rol_usuario values (2,1);
    insert into rol values(1, 'Asistente', 'Asistente en LAB');
    insert into rol values(2,'Jefe', 'Jefe de Departamento');
    insert into usuarios values (1, 'Alonso', 'Araya', 'correo@correo.com', 'caca');
    insert into usuarios values (2, 'Ignacio', 'Araya', 'correo@correo.com', 'mierda');
    
    
    select * from usuarios;
    select * from rol_usuario;
    select * from rol;
    set verify off;
    set serverOutput on;
    
    create or replace procedure creaClientes (idUsuario IN number, nomCli in varchar2, apelCli in varchar2, correoCli in varchar2, conCli in varchar2)
    is
    begin
    
    insert into usuarios values(idUsuario, nomCli, apelCli, correoCli, conCli);
    
    end;
    
    execute creaClientes (3, 'Paula', 'Calvo', 'correo@correo.com', 'cacaaa');
    
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
    
    execute borraCliente (3);
    
    create or replace procedure muestraClientes
    as
    muestraCursor sys_refcursor;
    begin
    open muestraCursor for
   select * from usuarios;
   
   dbms_sql.return_result(muestraCursor);
    end;
    
    
    execute muestraClientes;
    
    
      create or replace procedure muestraClienteEspecifico (idUsuario in number)
    as
    muestraCursorDos sys_refcursor;
    begin
    open muestraCursorDos for
   select * from usuarios
   where id_usuario=idUsuario;
   
   dbms_sql.return_result(muestraCursorDos);
    end;
    
    
    execute muestraClienteEspecifico (1);
    
    
create or replace procedure crearRol (idRol in number, nombreRol in varchar2, descripcionRol in varchar2)
is
begin

insert into rol values (idRol, nombreRol, descripcionRol);

end;

execute crearRol (100, 'Vago', 'Vagazo');
    
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

execute borrarRol (100);
    
    
    
    
