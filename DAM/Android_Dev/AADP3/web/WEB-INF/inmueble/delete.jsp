<%@page import="com.sergio.hibernate.Inmueble"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%
    Inmueble inmueble = (Inmueble) request.getAttribute("inmueble");
%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Borrado inmueble</title>
        <link href="faces/resources/css/tabla.css" rel="stylesheet" type="text/css" />
        <link href="faces/resources/css/estilos.css" rel="stylesheet" type="text/css" />
    </head>
    <body>
        <div class="cabecera">
            <h1>Inmobiliaria</h1>
            <h2>Confirmación delete</h2>
        </div>
        <div class="tabla">
            <table>
                <tr>
                    <td colspan="2">Datos del inmueble</td>
                </tr>
                <tr>
                    <td>ID</td>
                    <td><%= inmueble.getId()%></td>
                </tr>
                <tr>
                    <td>Tipo</td>
                    <td><%= inmueble.getTipo()%></td>
                </tr>
                <tr>
                    <td>Dirección</td>
                    <td><%= inmueble.getCalle() + " " + inmueble.getNumero()%></td>
                </tr>
                <tr>
                    <td>Localidad</td>
                    <td><%= inmueble.getLocalidad()%></td>
                </tr>
                <tr>
                    <td>Precio</td>
                    <td><%= inmueble.getPrecio()%></td>
                </tr>
                <tr>
                    <td>Usuario</td>
                    <td><%= inmueble.getUsuario()%></td>
                </tr>
            </table>
        </div>
        <div class="botones">
            <a href="control?target=inmueble&op=delete&action=op&id=<%= inmueble.getId()%>"><input type="button" value="Confirmar Borrado" name="confirmar" /></a>
            <a href="control?target=inmueble&op=select&action=view"><input type="button" value="Cancelar" name="cancelar" /></a>
        </div>
    </body>
</html>