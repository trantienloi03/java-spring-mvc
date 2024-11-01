<%@page contentType="text/html" pageEncoding="UTF-8"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
        <%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>delete user ${id}</title>
    <!-- Latest compiled and minified CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">

    <!-- Latest compiled JavaScript -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>

    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>

</head>
<body>
    <div class="container mt-5">
        <div class="row">
            <div class="col-md-6 col-12 mx-auto">
                <h2 style="color: red;">delete user ${UserDelete.id}</h2> <hr>
                <form:form method="POST" action="/admin/user/delete" modelAttribute="UserDelete">
                    <div class="mb-3" style="display: none;">
                        <label class="form-label">ID</label>
                        <form:input type="text" value="${id}" path="id" class="form-control" />
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Email address</label>
                        <form:input type="email" path="email" class="form-control" disabled="true"/>
                    </div>
                    <div class="mb-3">
                        <label for="fullName" class="form-label">Full name</label>
                        <form:input type="text" path="fullname" class="form-control" disabled="true" />
                    </div>
                    <div class="mb-3">
                        <label for="address" class="form-label">Address</label>
                        <form:input type="text" path="address" class="form-control" disabled="true"/>
                    </div>
                    <div class="mb-3">
                        <label for="phone" class="form-label">Phone</label>
                        <form:input type="text" path="phone" class="form-control" disabled="true"/>
                    </div>
                    <button type="submit" class="btn btn-danger">Confirm</button>
                </form:form>
            </div>
        </div>
        
    </div>
    
</body>
</html>