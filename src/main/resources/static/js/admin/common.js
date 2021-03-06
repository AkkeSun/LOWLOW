// ================ 전역변수 ==============
let totalPages = "";
let mAttendCreatListCnt = "";
let REST_API_URL = "http://sewoomch.com/api"


// ================= Ajax 처리 함수 ====================
function ajaxComm(type, data, url, async, csrfHeader, csrfToken) {
    var callback =
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

    var returnMsg = "";

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
    var data = formData;
    var obj = {};
    $.each(data, function(idx, ele){
        obj[ele.name] = ele.value;
    });
    return obj;
}



// ================= 공용 페이징처리 ====================
function commonPagingProcess(pageName){

    var nowPage = 1;

    $("#basicPagination").twbsPagination('destroy');
    $("#basicPagination").twbsPagination({
        startPage:  nowPage,
        totalPages: totalPages,
        visiblePages: 5,
        prev:"Prev",
        next:"Next",
        first:'<span sria-hidden="true">«</span>',
        last:'<span sria-hidden="true">»</span>',
        initiateStartPageClick:false,
        onPageClick:function(event, page){
            nowPage = page;
            commonListLoad(pageName,nowPage-1);
        }
    });
}


// ================= 공용 UPDATE VIEW 셋업 ====================
function commonUpdateViewSetting(pageName){

    $("#updateProcessBtn").show();     // 수정완료버튼
    $("#updateViewBtn").hide();        // 수정버튼
    $("#deleteBtn").hide();            // 삭제버튼

    switch(pageName){
        case 'members'       : memberUpdateViewSetting(); break;
        case 'accounting'    : accountingUpdateViewSetting(); break;
        case 'galleries'     : galleryUpdateViewSetting(); break;
        case 'calendars'     : calendarUpdateViewSetting(); break;
        case 'notices'       : noticeUpdateViewSetting(); break;
        case 'weekly'        : weeklyUpdateViewSetting(); break;
        case 'worshipVideos' : worshipVideoUpdateViewSetting(); break;
    }
}




// ================= 공용 INSERT & UPDATE 처리 ====================
function commonInsertAndUpdate(pageName, type){

    // param setting
    var csrfHeader  = $("#_csrf_header").attr('content');
    var csrfToken   = $("#_csrf").attr('content');
    var url         = `${REST_API_URL}/${pageName}`;
    var redirectUrl = `/admin/${pageName}`;
    var async       = true;
    var isSecurity  = false;
    var data        = {};


    // data setting
    switch(pageName){
        case "members" :
            data = objToJson($('#memberFrm').serializeArray());
            var imageObject   = memberImageProcess(csrfHeader, csrfToken);
            data.image = imageObject;
            break;
        case "accounting" :
            data = objToJson($('#accountingForm').serializeArray());
            break;
        case "galleries" :
            data = objToJson($('#galleryFrm').serializeArray());
            break;
        case "calendars" :
            data = objToJson($('#calendarFrm').serializeArray());
            break;
        case "notices" :
            data = objToJson($('#noticeFrm').serializeArray());
            break;
        case "weekly" :
            data = objToJson($('#weeklyFrm').serializeArray());
            var imageObject = weeklyImageProcess(csrfHeader, csrfToken);
            data.img1 = imageObject.image1;
            data.img2 = imageObject.image2;
            data.img3 = imageObject.image3;
            data.img4 = imageObject.image4;
            break;
        case "worshipVideos" :
            data = objToJson($('#worshipVideoFrm').serializeArray());
            data.link = youtubeLinkConverter(data.link);
            break;
    }

    // update url 변경
    if(type == 'put')
        url = `${REST_API_URL}/${pageName}/${$("#id").val()}`;


    // ajax
    var callback = ajaxComm(type, JSON.stringify(data), url, async, csrfHeader, csrfToken);

    callback.done( function(data){ ajaxCallbackProcess(isSecurity, data, type, redirectUrl) } );

    callback.fail ( function(xhr, status, error) {

        // 업로드 파일 삭제
        if(data.uploadName)
            ajaxFileDelete(csrfHeader, csrfToken, data.uploadName);

        var errorResource = JSON.parse(xhr.responseText).content[0];
        console.log("[ERROR STATUS] : " + xhr.status);
        console.log(errorResource);
        alert(errorResource.defaultMessage);
    });
}



// ================= 공용 DELETE 처리 ====================
function commonDelete(pageName){

    var check = confirm('정말 삭제하시겠습니까');
    if(check) {

        // param setting
        var csrfHeader  = $("#_csrf_header").attr('content');
        var csrfToken   = $("#_csrf").attr('content');
        var type        = "delete";
        var url         = `${REST_API_URL}/${pageName}/${$("#id").val()}`;
        var redirectUrl = `/admin/${pageName}`;
        var async       = true;
        var isSecurity  = false;
        var data        = "";

        // ajax process
        var callback = ajaxComm(type, data, url, async, csrfHeader, csrfToken);
        callback.done( function(data) { ajaxCallbackProcess(isSecurity, data, type, redirectUrl)});
    }
}





// ================= 공용 리스트 로드 ====================
function commonListLoad(pageName, nowPage){

    switch(pageName){
        case 'members'       : memberListLoad(nowPage); break;
        case 'accounting'    : accountingListLoad(nowPage); break;
        case 'galleries'     : galleryListLoad(nowPage); break;
        case 'notices'       : noticeListLoad(nowPage); break;
        case 'worshipVideos' : worshipVideoListLoad(nowPage); break;
        case 'weekly'        : weeklyListLoad(nowPage); break;
        case 'mAttendList'   : getM_AttendList(nowPage); break;
        case 'mAttendCreate' : getM_AttendCreateTable(nowPage); break;
        case 'mAttendDetail' : getM_AttendUpdateTable(); break;
    }
}




// ================ 공용 검색기능 ================
function commonSearch(pageName){
    switch(pageName){
        case 'members' :
            commonPagingProcess('members');
            memberListLoad(0);
            break;
        case 'accounting' :
            commonPagingProcess('accounting');
            accountingDataLoad(0);
            break;
        case 'galleries' :
            commonPagingProcess('gallery');
            galleryListLoad(0);
            break;
        case 'notices' :
            commonPagingProcess('notices');
            noticeListLoad(0);
            break;
        case 'weekly' :
            commonPagingProcess('weekly');
            weeklyListLoad(0);
            break;
        case 'worshipVideos' :
            commonPagingProcess('worshipVideos');
            worshipVideoListLoad(0);
            break;
    }
}





// ================ 공용 검색기능 초기화================
function commonSearchInitialize(pageName){
    switch(pageName){
        case 'members' :
            $("#searchId").val("name");
            $("#searchData").val("");
            memberListLoad(0);
            commonPagingProcess('member');
            break;
        case 'accounting' :
            $("#searchId").val("name");
            $("#searchData").val("");
            $("#startDate").val(null);
            $("#endDate").val(null);
            accountingDataLoad(0);
            commonPagingProcess('accounting');
            break;
        case 'galleries' :
            $("#searchId").val("title");
            $("#searchData").val("");
            galleryListLoad(0);
            commonPagingProcess('gallery');
            break;
        case 'notices' :
            $("#searchId").val("title");
            $("#searchData").val("");
            noticeListLoad(0);
            commonPagingProcess('notices');
            break;
        case 'weekly' :
            $("#searchId").val("title");
            $("#searchData").val("");
            weeklyListLoad(0);
            commonPagingProcess('weekly');
            break;
        case 'worshipVideos' :
            $("#searchId").val("title");
            $("#searchData").val("");
            worshipVideoListLoad(0);
            commonPagingProcess('worshipVideos');
            break;
    }
}




// ================= 파일 업로드 처리함수 ====================
function ajaxFileUpload (csrfHeader, csrfToken, data, folder){
    var callback =
        $.ajax({
            type       : "post",
            url        : "/file/upload/"+folder,
            enctype    : false,
            async      : false,
            processData: false,
            contentType: false,
            data       : data,
            beforeSend : xhr  => { xhr.setRequestHeader(csrfHeader, csrfToken) }
        });
    return callback;
};





// ================= 파일 삭제 처리함수 ====================
function ajaxFileDelete (csrfHeader, csrfToken, uploadFileName){

    var callback =
        $.ajax({
            type       : "post",
            url        : "/file/delete",
            async      : false,
            data       : {"uploadFileName":uploadFileName},
            beforeSend : xhr  => { xhr.setRequestHeader(csrfHeader, csrfToken) }
        });
    return callback;
};





// ================= DatePicker 셋팅 함수 ====================
function datePickerSet(date) {

    date.datepicker({
        format : "yyyy-mm-dd",
        todayHighlight : true,
        autoclose : true,
        language : "ko"
    });
}





// ================= 서머노트 사용함수 ====================
function useSummernote(bbsType) {
    $('#contents').summernote({
        placeholder: '내용을 입력하세요',
        height: 500,
        minHeight: null,
        maxHeight: null,
        lang: "ko-KR",
        // 안쓰는 메뉴 주석처리
        toolbar: [
            ['fontname', ['fontname']],
            ['fontsize', ['fontsize']],
            ['style', ['bold', 'italic', 'underline','strikethrough', 'clear']],
            ['color', ['foreclose','color']],
            //   ['table', ['table']],
            ['para', ['ul', 'ol', 'paragraph']],
            ['height', ['height']],
            ['insert',['picture', /* 'link','video' */]],
            ['view', ['codeview',/*'fullscreen', 'help'*/]]
        ],
        fontNames: ['Arial', 'Arial Black', 'Comic Sans MS', 'Courier New','맑은 고딕','궁서','굴림체','굴림','돋움체','바탕체'],
        fontSizes: ['8','9','10','11','12','14','16','18','20','22','24','28','30','36','50','72'],
        callbacks: {
            onImageUpload: function (files) {
                for (var i = files.length - 1; i >= 0; i--) {
                    uploadSummernoteImageFile(files[i], this, bbsType);
                }
            }
        }
    });
}


// ================= 서머노트 파일 업로드 함수 ====================
function uploadSummernoteImageFile(file, el, bbsType) {

    //========= 파일 사이즈 체크 =========
    var maxSize = 5 * 1024 * 1024; // 5MB
    var browser=navigator.appName;

    if (browser == "Microsoft Internet Explorer") {
        var oas = new ActiveXObject("Scripting.FileSystemObject");
        fileSize = oas.getFile(f.value).size;
    }
    else
        fileSize = file.size;

    if (fileSize > maxSize) {
        alert("첨부파일 사이즈는 5MB 이내로 등록 가능합니다.");
        return;
    }

    data = new FormData();
    data.append("image", file);
    var csrfHeader  = $("#_csrf_header").attr('content');
    var csrfToken   = $("#_csrf").attr('content');
    var callback = ajaxFileUpload(csrfHeader, csrfToken, data, "summernote");

    callback.done(data => {

        // aws s3 upload 인 경우
       if(data.image.fullUrl)
        {
            var image = $('<img>').attr('src', '' + data.image.fullUrl);
            $('#contents').summernote("insertNode", image[0]);
        }
        // 배포 서버에 업로드한 경우
        else
        {
            $('#contents').summernote("insertImage", "/upload/summernote/"+data.image.uploadName);
        }

        // 후에 SummerNote 에서 사용하지 않는 이미지를 삭제하는 batch 를 위해 DB에 저장
        var ajaxData = {};
        ajaxData.uploadName = data.image.uploadName;
        ajaxData.originalName = data.image.originalName;
        ajaxData.bbsType = bbsType;

        ajaxComm("post", JSON.stringify(ajaxData), REST_API_URL+"/summerNote", 'true', csrfHeader, csrfToken);
    })
}



// ================= Null 인경우 '-' 리턴 ====================
function nullConverter(data){
    if(data == null) return "-";
    else return data;
}

