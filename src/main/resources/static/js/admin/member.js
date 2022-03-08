
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
    var type        = "get";
    var data        = "searchId="+$("#searchId").val()+"&searchData="+$("#searchData").val()+"&nowPage="+nowPage;
    var url         = "/api/members";
    var csrfHeader  = $("#_csrf_header").attr('content');
    var csrfToken   = $("#_csrf").attr('content');
    var async       = false;
    var callback    = ajaxComm(type, data, url, async, csrfHeader, csrfToken);

    callback.done( (data) => {

        totalPages = 1;

        var appendData = "";
        if( data._embedded ) {
            totalPages = data.page.totalPages;

            var list = data._embedded.memberList;
            list.forEach(function (data, index) {
                let churchOfficer = churchOfficerChangeToKor(data.churchOfficer);
                appendData += `
                               <tr id="appendItem">
                                    <td style="text-align: left;">
                                        ${index+1}
                                    </td>
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

        $("tr").remove("#appendItem");       // 기존 데이터 삭제
        $("#appendPath").append(appendData); // data append

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
    $("#image").removeAttr("disabled");
};






// ================= 파일 사이즈 체크 ====================
function memberFileCheck(e){

    let files = e.target.files;
    let filesArr = Array.prototype.slice.call(files);

    filesArr.forEach(function(f) {

        //========= 이미지 종류 체크 =========
        if(!f.type.match("image.*")) {
            alert("이미지만 업로드 가능합니다.");
            return;
        }

        //========= 파일 사이즈 체크 =========
        var maxSize = 5 * 1024 * 1024; // 5MB
        var browser=navigator.appName;

        if (browser == "Microsoft Internet Explorer") {
            var oas = new ActiveXObject("Scripting.FileSystemObject");
            fileSize = oas.getFile(f.value).size;
        }
        else
            fileSize = f.size;

        if (fileSize > maxSize) {
            alert("첨부파일 사이즈는 5MB 이내로 등록 가능합니다.");
            $(this).val('');
            $("#imageView").attr('src', '/image/uploadSample.png');
            return;
        }

        //========= 이미지 미리보기 기능 구현 =========
        sel_file = f;
        let reader = new FileReader();
        reader.onload = function(e) {
            $("#imageView").attr("src", e.target.result);
            $("#imageView").css("width", '150px');
        }
        reader.readAsDataURL(f);
    });
}






// ================= Member Image Upload Process ====================
function memberImageProcess(csrfHeader, csrfToken) {
    imageObject = {};

    // Create : 이미지를 업로드하는 경우
    if($("#image").val()){
        var fileUploadCallback = ajaxFileUpload(csrfHeader, csrfToken, new FormData($("#memberFrm")[0]), "member");
        fileUploadCallback.done( uploadData => {
            imageObject.uploadName =  uploadData.image.uploadName;
            imageObject.originalName =  uploadData.image.originalName;
        });
    }

    // Update : 이미지를 수정하지 않는 경우
    if($("#image").val() == "" && $("#savedOriginalName").val()){
        imageObject.uploadName  = $("#savedUploadName").val();
        imageObject.originalName  = $("#savedOriginalName").val();
    }
    return imageObject;
}



