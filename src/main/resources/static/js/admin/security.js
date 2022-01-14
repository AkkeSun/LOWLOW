
// ================= Tab 컨트롤 ====================
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



// ================= security Paging 처리  ====================
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



// ================= security formData 셋팅  ====================
function securityFormDataSetting(database){
    switch (database){
        case "account" :
            return JSON.stringify(objToJson($("#accountFrm").serializeArray()));
        case "role"     :
            return JSON.stringify(objToJson($('#roleFrm').serializeArray()));
        case "resource" :
            return JSON.stringify(objToJson($('#resourceFrm').serializeArray()));
    }
}



// ================= security redirect url 셋팅  ====================
function securityRedirectUrlSetting(database){
    switch (database){
        case "account" :
            return "/admin/security"
        case "role"     :
            return  "/admin/security?tab=2"
        case "resource" :
            return "/admin/security?tab=3";
    }
}



// ================= security create  ====================
function securityCreateFunc(database){

    var type        = "post";
    var url         = "/admin/security/"+database;
    var csrfHeader  = $("#_csrf_header").attr('content');
    var csrfToken   = $("#_csrf").attr('content');
    var redirectUrl = securityRedirectUrlSetting(database);
    var data        = securityFormDataSetting(database);
    var async       = true;
    var isSecurity  = true;

    var callback = ajaxComm(type, data, url, async, csrfHeader, csrfToken);
    callback.done( function(data) { ajaxCallbackProcess(isSecurity, data, type, redirectUrl) });
}



// ================= security update  ====================
function securityUpdateFunc(database){

    var type        = "put";
    var url         = "/admin/security/"+ database +"/" + $("#id").val();
    var csrfHeader  = $("#_csrf_header").attr('content');
    var csrfToken   = $("#_csrf").attr('content');
    var redirectUrl = securityRedirectUrlSetting(database);
    var data        = securityFormDataSetting(database);
    var async       = true;
    var isSecurity  = true;

    var callback = ajaxComm(type, data, url, async, csrfHeader, csrfToken);
    callback.done( function(data) { ajaxCallbackProcess(isSecurity, data, type, redirectUrl) });
}



// ================= security delete  ====================
function securityDeleteFunc(database){

    var check = confirm('정말 삭제하시겠습니까');
    if(check) {
        var type        = "delete";
        var url         = "/admin/security/"+ database +"/" + $("#id").val();
        var csrfHeader  = $("#_csrf_header").attr('content');
        var csrfToken   = $("#_csrf").attr('content');
        var redirectUrl = securityRedirectUrlSetting(database);
        var async       = true;
        var isSecurity  = true;

        var callback = ajaxComm(type, "", url, async, csrfHeader, csrfToken);
        callback.done( function(data) { ajaxCallbackProcess(isSecurity, data, type, redirectUrl) });
    }
}

