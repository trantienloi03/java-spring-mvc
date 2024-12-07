<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8" />
        <meta http-equiv="X-UA-Compatible" content="IE=edge" />
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no" />
        <meta name="description" content="" />
        <meta name="author" content="" />
        <title>Create User - SB Admin</title>
        <link href="/css/styles.css" rel="stylesheet" />
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>

                <script>
                    $(document).ready(() => {
                        const avatarFile = $("#avatarFile");
                        avatarFile.change(function (e) {
                            const imgURL = URL.createObjectURL(e.target.files[0]);
                            $("#avatarPreview").attr("src", imgURL);
                            $("#avatarPreview").css({ "display": "block" });
                        });
                    });
                </script>

        <script src="https://use.fontawesome.com/releases/v6.3.0/js/all.js" crossorigin="anonymous"></script>
    </head>
    <body class="sb-nav-fixed">
        <jsp:include page="../layout/header.jsp"/>
        <div id="layoutSidenav">
        <jsp:include page="../layout/sidebar.jsp"/>
            <div id="layoutSidenav_content">
                <main>
                    <div class="container-fluid px-4">
                        <h1 class="mt-4">Manage product</h1>
                        <ol class="breadcrumb mb-4">
                            <li class="breadcrumb-item"> <a href="/admin">Dashboard</a></li>
                            <li class="breadcrumb-item active">product</li>
                        </ol>
                        <div class="mt-5">
                            <div class="row">
                                <div class="col-md-6 col-12 mx-auto">
                                    <h2>Create a product</h2> <hr>
                                    <form:form class="row" action="/admin/product/create" method="Post" enctype="multipart/form-data"
                                              modelAttribute="NewProduct">
                                          <c:set var="nameHasError">
                                            <form:errors path="name" cssClass="invalid-feedback"/>
                                          </c:set>
                                          <c:set var="priceHasError">
                                            <form:errors path="price" cssClass="invalid-feedback"/>
                                          </c:set>
                                          <c:set var="detailDescHasError">
                                            <form:errors path="detailDesc" cssClass="invalid-feedback"/>
                                          </c:set>
                                          <c:set var="shortDescHasError">
                                            <form:errors path="shortDesc" cssClass="invalid-feedback"/>
                                          </c:set>
                                          <c:set var="quantityHasError">
                                            <form:errors path="quantity" cssClass="invalid-feedback"/>
                                          </c:set>

                        
                                        <div class="mb-3 col-12 col-md-6">
                                          <label for="name" class="form-label">Name:</label>
                                          <form:input type="text" path="name" class="form-control ${not empty nameHasError ? 'is-invalid' : ''}" id="name"/>
                                          ${nameHasError}
                                        </div>
                                        <div class="mb-3 col-12 col-md-6">
                                          <label for="price" class="form-label">Price:</label>                                        
                                          <form:input type="number" path="price" class="form-control ${not empty priceHasError ? 'is-invalid' : ''}" id="price"/>
                                          ${priceHasError}
                                        </div>
                                        <div class="mb-3 col-12">
                                          <label for="detailDesc" class="form-label">Detail description:</label>
                                          <form:textarea type="text" path="detailDesc" class="form-control ${not empty detailDescHasError ? 'is-invalid' : ''}" id="detailDesc"/>
                                          ${detailDescHasError}
                                        </div>
                                        <div class="mb-3 col-12 col-md-6">
                                          <label for="shortDesc" class="form-label">Detail short:</label>
                                          <form:input type="text" path="shortDesc" class="form-control ${not empty shortDescHasError ? 'is-invalid' : ''}" id="shortDesc"/>
                                          ${shortDescHasError}
                                        </div>
                                        <div class="mb-3 col-12 col-md-6">
                                            <label for="quantity" class="form-label">Quantity:</label>
                                            <form:input type="number" path="quantity" class="form-control ${not empty quantityHasError ? 'is-invalid' : ''}" id="quantity"/>
                                            ${quantityHasError}
                                          </div>
                                        <div class="mb-3 col-12 col-md-6">
                                            <label for="factory" class="form-label">Factory:</label>
                                            <form:select id="factory" class="form-select" path="factory">
                                              <form:option value="Macbook">Macbook</form:option>
                                              <form:option value="Asus">Asus</form:option>
                                              <form:option value="Dell">Dell</form:option>
                                              <form:option value="Lennovo">Lennovo</form:option>
                                              <form:option value="HP">HP</form:option>
                                            </form:select>
                                            
                                          </div>
                                          <div class="mb-3 col-12 col-md-6">
                                            <label for="target" class="form-label">Target:</label>
                                            <form:select id="target" class="form-select" path="target">
                                              <form:option value="Office">office</form:option>
                                              <form:option value="Gaming">gaming</form:option>
                                            </form:select>
                                       
                                          </div>
                                        <div class="mb-3 col-12 col-md-6">
                                            <label for="avatarFile" class="form-label">Image:</label>
                                            <input class="form-control" type="file" id="avatarFile" 
                                                    accept=".png, .jpg, .jpeg" name="trantienloiFile"/>
                                        </div>
                                        <div class="mb-3 col-12">
                                           <img style="max-height: 250px; display: none;" alt="avatar preview"
                                                id="avatarPreview">
                                        </div>
                                        <div class="col-12 mb-5">
                                          <button type="submit" class="btn btn-primary">Create</button>
                                        </div>
                                      </form:form>
                                </div>
                            </div>
                            
                        </div>
                    
                    
                    </div>
                </main>
               <jsp:include page="../layout/footer.jsp" />
            </div>
        </div>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js" crossorigin="anonymous"></script>
        <script src="/js/scripts.js"></script>
    </body>
</html>
