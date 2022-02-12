<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<c:if test="${utilisateur eq null}">
	<jsp:forward page="index"></jsp:forward>
</c:if>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Gif Televerse</title>
<link href="style/${utilisateur.theme.nom.toLowerCase()}.css"
	rel="stylesheet">
</head>
<body>
	<h1>Televerser un gif</h1>
	<h1>Placer un gif distant sur le ${gifTeleverse.jour.date} (nombre
		de points: ${gifTeleverse.jour.nbPoints})</h1>
	<form:form modelAttribute="gifTeleverse"
		action="placerGifTeleverser?ID_JOUR=${jour.date}" method="post"
		enctype="multipart/form-data">
		<input type="file" name="FICHIER" accept="image/gif" />
		<input type="text" name="LEGENDE" />
		<input type="submit" value="Envoyer" />
	</form:form>

</body>
</html>