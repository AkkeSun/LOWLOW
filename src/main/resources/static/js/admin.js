// security Tab 컨트롤
function securityTabControl(nowTab){

    if( nowTab ) {
        if(nowTab == 'tab2'){
            $("#tab1").removeClass('active');
            $("#user").removeClass('active').addClass('fade');
            $("#tab2").removeClass('fade').addClass('active');
            $("#role").removeClass('fade').addClass('active');
        }
        if(nowTab == 'tab3'){
            $("#tab1").removeClass('active');
            $("#user").removeClass('active').addClass('fade');
            $("#tab3").removeClass('fade').addClass('active');
            $("#resource").removeClass('fade').addClass('active');
        }
    }
}

// security Paging 처리 
function securityPaging( accountPagingData, rolePagingData, resourcePagingData ){

    $("#accountPagination").twbsPagination('destroy');
    $("#accountPagination").twbsPagination({
        startPage:  accountPagingData.nowPage,
        totalPages: accountPagingData.totalPages,
        visiblePages: 5,
        prev:"Prev",
        next:"Next",
        first:'<span sria-hidden="true">«</span>',
        last:'<span sria-hidden="true">»</span>',
        initiateStartPageClick:false,
        onPageClick:function(event, page){
            location.href="/admin/security/pagingReload?dbName=account&num="+page;
        }
    });

    $("#rolePagination").twbsPagination('destroy');
    $("#rolePagination").twbsPagination({
        startPage:  rolePagingData.nowPage,
        totalPages: rolePagingData.totalPages,
        visiblePages: 5,
        prev:"Prev",
        next:"Next",
        first:'<span sria-hidden="true">«</span>',
        last:'<span sria-hidden="true">»</span>',
        initiateStartPageClick:false,
        onPageClick:function(event, page){
            location.href="/admin/security/pagingReload?dbName=role&num="+page;
        }
    });

    $("#resourcePagination").twbsPagination('destroy');
    $("#resourcePagination").twbsPagination({
        startPage:  resourcePagingData.nowPage,
        totalPages: resourcePagingData.totalPages,
        visiblePages: 5,
        prev:"Prev",
        next:"Next",
        first:'<span sria-hidden="true">«</span>',
        last:'<span sria-hidden="true">»</span>',
        initiateStartPageClick:false,
        onPageClick:function(event, page){
            location.href="/admin/security/pagingReload?dbName=resource&num="+page;
        }
    });
}

// JSON 공용 처리 함수
function ajaxComm(type, data, url, redirectUrl, csrfHeader, csrfToken) {

    let returnMsg = "";
    switch(type){
        case "post"    : returnMsg="성공적으로 등록되었습니다"; break;
        case "put"     : returnMsg="성공적으로 수정되었습니다"; break;
        case "delete"  : returnMsg="성공적으로 삭제되었습니다";
    }

    let callback =
        $.ajax({
            type       : type,
            url        : url,
            data       : JSON.stringify(data), // object -> json String
            beforeSend: function (xhr) {
                xhr.setRequestHeader("Content-type", "application/json");
                xhr.setRequestHeader(csrfHeader, csrfToken)
            }
        });

    callback.done( data => {
        if(data != 'success')
            alert(data);
        else{
            alert(returnMsg);
            location.href = redirectUrl;
        }
    });
}



// serializeArray() -> Json Object
function objToJson(formData){
    var data = formData;
    var obj = {};
    $.each(data, function(idx, ele){
        obj[ele.name] = ele.value;
    });
    return obj;
}


// Common Create Function
function createFunc(database){

    let url         = "/admin/security/"+database;
    let csrfHeader  = $("#_csrf_header").attr('content');
    let csrfToken   = $("#_csrf").attr('content');
    let redirectUrl = "";
    let data        = "";

    switch (database){
        case "account" :
            data = objToJson($("#accountFrm").serializeArray());
            redirectUrl = "/admin/security"
            break;
        case "role"     :
            data = objToJson($('#roleFrm').serializeArray());
            redirectUrl = "/admin/security?tab=2"
            break;
        case "resource" :
            data = objToJson($('#resourceFrm').serializeArray());
            redirectUrl = "/admin/security?tab=3";
    }

    ajaxComm("post", data, url, redirectUrl, csrfHeader, csrfToken);
}



// Common Update Function
function updateFunc(database){

    let url         = "/admin/security/"+ database +"/" + $("#id").val();
    let csrfHeader  = $("#_csrf_header").attr('content');
    let csrfToken   = $("#_csrf").attr('content');
    let redirectUrl = "";
    let data        = "";

    switch (database){
        case "account" :
            data = objToJson($("#accountFrm").serializeArray());
            redirectUrl = "/admin/security"
            break;
        case "role"     :
            data = objToJson($('#roleFrm').serializeArray());
            redirectUrl = "/admin/security?tab=2"
            break;
        case "resource" :
            data = objToJson($('#resourceFrm').serializeArray());
            redirectUrl = "/admin/security?tab=3";
    }

    ajaxComm("put", data, url, redirectUrl, csrfHeader, csrfToken);
}


// Common Delete Function
function deleteFunc(database){
    let check = confirm('정말 삭제하시겠습니까');
    if(check) {

        let url         = "/admin/security/"+ database +"/" + $("#id").val();
        let csrfHeader  = $("#_csrf_header").attr('content');
        let csrfToken   = $("#_csrf").attr('content');
        let redirectUrl = "";

        switch (database){
            case "account"  : redirectUrl = "/admin/security";        break;
            case "role"     : redirectUrl = "/admin/security?tab=2";  break;
            case "resource" : redirectUrl = "/admin/security?tab=3";
        }

        ajaxComm("delete", "", url, redirectUrl, csrfHeader, csrfToken);
    }
}