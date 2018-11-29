set verify off;
set serveroutput on;
 
create or replace function verificaConexion(
               correo in varchar2,
                contra in varchar2)
     return varchar2
     as
        iduser number;
        contrasenaSis varchar2(30);
        mensaje varchar2(20);
 begin
    select  id_usuario into iduser from usuarios
    where CORREO_USUARIO= correo;
    if iduser is not null then
      select contrasena into contrasenaSis from usuarios
      where id_usuario=iduser;
      if contrasenaSis is not null then
        if contrasenaSis=contra then
          mensaje:='correcto';
        else
          mensaje:='error';
        end if;
       else
        mensaje:='error';
       end if; 
    else 
      mensaje:='error';
    end if;
   return mensaje;
end verificaConexion;  








create or replace procedure verificaCon ( 
                correo in varchar2,
                contra  in  varchar2,
                mensaje out varchar2)as
begin
  mensaje:=verificaConexion(correo, contra); 
end verificaCon; 

declare
mens varchar2(100);
begin
verificaCon('correo@correo.com','caca', mens);
dbms_output.put_line('Esto es mens'||mens);
end;
                       
create or replace procedure setUser
                      (correo in varchar2,
                       usuario out varchar2,
                       iduser out number,
                       rol out number)
  as
    cursor datos_usuario is
      select  u.NOMBRE_USUARIO, u.APELLIDOS_USUARIO, r.id_rol, u.id_usuario 
      from usuarios u, rol_usuario r
      where CORREO_USUARIO= correo
      and u.id_usuario=r.id_usuario;
    nombreUser varchar2(20);
    apellidosUser varchar2(60);
    roluser number;
    idUsuario number; 
  begin
    open datos_usuario;
      fetch datos_usuario into nombreUser, apellidosUser, rol, idUser;
      usuario:= nombreUser||' '||apellidosUser;
    close datos_usuario;
  end setUser;       
  
  
 
 




































