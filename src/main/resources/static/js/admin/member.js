
// ================= 직분 한글 변경 함수  ====================
function churchOfficerChangeToKor(churchOfficer){

    switch( churchOfficer ){
        case "LAYMAN"              : return '평신도';
        case "DEACON"              : return '집사';
        case "ORDAINED_DEACON"     : return '안수집사';
        case "SENIOR_DEACONESS"    : return '권사';
        case "ELDER"               : return '장로';
        case "JUNIOR_PASTOR"       : return '전도사';
        case "ASSISTANT_PASTOR"    : return '부목사';
        case "SENIOR_PASTOR"       : return '담임목사';
        case "WIFE"                : return '사모';
    };
}


// ================= MemberList Data Load  ====================
function memberListLoad(nowPage){

    // param setting
    let type        = "get";
    let data        = "searchId="+$("#searchId").val()+"&searchData="+$("#searchData").val()+"&nowPage="+nowPage;
    let url         = "/api/members";
    let csrfHeader  = $("#_csrf_header").attr('content');
    let csrfToken   = $("#_csrf").attr('content');
    let async       = false;
    let callback    = ajaxComm(type, data, url, async, csrfHeader, csrfToken);

    callback.done( (data) => {

        // total page setting (global parameter)
        totalPages = data.page.totalPages;

        let appendData = "";
        if( data._embedded ) {

            let list = data._embedded.memberList;
            list.forEach(function (data, index) {
                let churchOfficer = churchOfficerChangeToKor(data.churchOfficer);
                appendData += `
                               <tr id="appendItem">
                                    <td style="text-align: left;">
                                        ${data.belong}
                                    </td>
                                    <td style="text-align: left;">
                                        <a href='/admin/members/${data.id}'>
                                            ${data.name}
                                        </a>
                                    </td>
                                    <td style="text-align: left;">
                                        ${data.phoneNumber}
                                    </td>
                                    <td style="text-align: left;">
                                        ${data.birthDay}
                                    </td>
                                    <td style="text-align: left;">
                                        ${churchOfficer}
                                    </td>
                                    <td style="text-align: left;">
                                        ${data.regiDate}
                                    </td>
                                </tr>
                                `
            });
        }

        $("#searchData").val("");            // 검색내용 삭제
        $("tr").remove("#appendItem");       // 기존 데이터 삭제
        $("#appendPath").append(appendData); // data append

    });
};




// ================= Member paging process ====================
function memberPagingProcess(){

    let nowPage = 1;

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
            memberListLoad(nowPage-1);
        }
    });
}



// ================ Member List Search ================
function memberSearch(){
    memberListLoad(0);
    memberPagingProcess();
}




// ================= Member Create & Update process  ====================
function memberCreateAndUpdateProcess(type){

    // param setting
    let csrfHeader  = $("#_csrf_header").attr('content');
    let csrfToken   = $("#_csrf").attr('content');
    let url         = "/api/members";
    let redirectUrl = "/admin/members";
    let async       = true;
    let isSecurity  = false;
    let data        = objToJson($('#withFileUploadFrm').serializeArray());

    // update url 변경
    if(type == 'put')
        url = "/api/members/"+$("#id").val();

    // 이미지 업로드
    if($("#image").val()){
        let fileUploadCallback = ajaxFileUpload(csrfHeader, csrfToken);
        fileUploadCallback.done( uploadData => {
            data.uploadName   = uploadData.uploadName;
            data.originalName = uploadData.originalName;
        });
    }

    // ajax process
    let callback = ajaxComm(type, JSON.stringify(data), url, async, csrfHeader, csrfToken);

    callback.done( data => ajaxCallbackProcess(isSecurity, data, type, redirectUrl) );

    callback.fail ( (xhr, status, error) => {

        // 업로드 파일 삭제
        ajaxFileDelete(csrfHeader, csrfToken, data.uploadName);

        let errorResource = JSON.parse(xhr.responseText).content[0];
        console.log("[ERROR STATUS] : " + xhr.status);
        console.log(errorResource);
        alert(errorResource.defaultMessage);
    });

};




// ================= Member Update View Setting ====================
function memberUpdateViewSetting() {
    $("#name").removeAttr("disabled");
    $("#phoneNumber").removeAttr("disabled");
    $("#gender").removeAttr("disabled");
    $("#birthDay").removeAttr("disabled");
    $("#belong").removeAttr("disabled");
    $("#name").removeAttr("disabled");
    $("#regiDate").removeAttr("disabled");
    $("#churchOfficer").removeAttr("disabled");
    $("#datePicker").removeAttr("disabled");

    $("#showImg").hide();
    $("#memberUpdateView").hide();
    $("#memberDelete").hide();
    $("#memberUpdate").show();
    $("#uploadImg").show();
};




// ================= Member Delete Process ====================
function memberDelete(){

    let check = confirm('정말 삭제하시겠습니까');
    if(check) {

        // param setting
        let csrfHeader  = $("#_csrf_header").attr('content');
        let csrfToken   = $("#_csrf").attr('content');
        let type        = "delete";
        let url         = "/api/members/"+$("#id").val();
        let redirectUrl = "/admin/members";
        let async       = true;
        let isSecurity  = false;
        let data        = "";

        // ajax process
        let callback = ajaxComm(type, data, url, async, csrfHeader, csrfToken);
        callback.done(data => ajaxCallbackProcess(isSecurity, data, type, redirectUrl));
    }

}