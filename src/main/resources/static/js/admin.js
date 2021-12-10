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