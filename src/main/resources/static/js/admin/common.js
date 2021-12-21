// ================ 전역변수 ==============
let totalPages = "";



// ================= Ajax 처리 함수 ====================
function ajaxComm(type, data, url, async, csrfHeader, csrfToken) {
    let callback =
        $.ajax({
            type       : type,
            url        : url,
            enctype    : 'multipart/form-data',
            data       : data,
            async      : async,
            beforeSend: function (xhr) {
                xhr.setRequestHeader("Content-type", "application/json");
                xhr.setRequestHeader(csrfHeader, csrfToken)
            }
        });
    return callback;
}




// ================= Ajax 콜백 처리 함수 ====================
function ajaxCallbackProcess(isSecurity, data, type, redirectUrl){

    let returnMsg = "";

    switch(type)
    {
        case "post"    : returnMsg="성공적으로 등록되었습니다"; break;
        case "put"     : returnMsg="성공적으로 수정되었습니다"; break;
        case "delete"  : returnMsg="성공적으로 삭제되었습니다";
    }

    if(isSecurity)
    {
        if(data != 'success')
            alert(data);
        else{
            alert(returnMsg);
            location.href = redirectUrl;
        }
    }
    else
    {
        alert(returnMsg);
        location.href = redirectUrl;
    }
}




// ================= serializeArray() -> Json Object ====================
function objToJson(formData){
    let data = formData;
    let obj = {};
    $.each(data, function(idx, ele){
        obj[ele.name] = ele.value;
    });
    return obj;
}



// ================= 파일 업로드 처리함수 ====================
function ajaxFileUpload (csrfHeader, csrfToken){

    let callback =
        $.ajax({
            type       : "post",
            url        : "/file/upload",
            enctype    : 'multipart/form-data',
            async      : false,
            processData: false,
            contentType: false,
            data       : new FormData($("#withFileUploadFrm")[0]),
            beforeSend : xhr  => { xhr.setRequestHeader(csrfHeader, csrfToken) }
        });
    return callback;
};


// ================= 파일 삭제 처리함수 ====================
function ajaxFileDelete (csrfHeader, csrfToken, uploadFileName){

    let callback =
        $.ajax({
            type       : "post",
            url        : "/file/delete",
            async      : false,
            data       : {"uploadFileName":uploadFileName},
            beforeSend : xhr  => { xhr.setRequestHeader(csrfHeader, csrfToken) }
        });
    return callback;
};
