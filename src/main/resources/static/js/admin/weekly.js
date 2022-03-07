

// ================= weeklyList Data Load  ====================
function weeklyListLoad(nowPage){

    // param setting
    var type        = "get";
    var data        = "searchId="+$("#searchId").val()+"&searchData="+$("#searchData").val()+"&nowPage="+nowPage;
    var url         = "/api/weekly";
    var csrfHeader  = $("#_csrf_header").attr('content');
    var csrfToken   = $("#_csrf").attr('content');
    var async       = false;
    var callback    = ajaxComm(type, data, url, async, csrfHeader, csrfToken);

    callback.done( (data) => {

        totalPages = 1;

        var appendData = "";
        if( data._embedded ) {
            totalPages = data.page.totalPages;

            var list = data._embedded.weeklyList;
            list.forEach(function (data, index) {
                appendData += `
                               <tr id="appendItem">
                                    <td style="text-align: left;">
                                        ${index+1}
                                    </td>
                                    <td style="text-align: left;">
                                        <a href='/admin/weekly/${data.id}'>
                                            ${data.title}
                                        </a>
                                    </td>
                                    <td style="text-align: left;">
                                        ${data.weeklyDate}
                                    </td>
                                </tr>
                                `
            });
        }

        $("tr").remove("#appendItem");       // 기존 데이터 삭제
        $("#appendPath").append(appendData); // data append

    });
};





// ================= Weekly Update View Setting ====================
function weeklyUpdateViewSetting() {
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




// ================= 파일 사이즈 체크 1 ====================
function weeklyFileCheck1(e){

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
            $("#imageView1").attr('src', '/image/uploadSample.png');
            return;
        }

        //========= 이미지 미리보기 기능 구현 =========
        sel_file = f;
        let reader = new FileReader();
        reader.onload = function(e) {
            $("#imageView1").attr("src", e.target.result);
            $("#imageView1").css("width", '150px');
        }
        reader.readAsDataURL(f);
    });
}




// ================= 파일 사이즈 체크 2 ====================
function weeklyFileCheck2(e){

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
            $("#imageView2").attr('src', '/image/uploadSample.png');
            return;
        }

        //========= 이미지 미리보기 기능 구현 =========
        sel_file = f;
        let reader = new FileReader();
        reader.onload = function(e) {
            $("#imageView2").attr("src", e.target.result);
            $("#imageView2").css("width", '150px');
        }
        reader.readAsDataURL(f);
    });
}


// ================= 파일 사이즈 체크 3 ====================
function weeklyFileCheck3(e){

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
            $("#imageView3").attr('src', '/image/uploadSample.png');
            return;
        }

        //========= 이미지 미리보기 기능 구현 =========
        sel_file = f;
        let reader = new FileReader();
        reader.onload = function(e) {
            $("#imageView3").attr("src", e.target.result);
            $("#imageView3").css("width", '150px');
        }
        reader.readAsDataURL(f);
    });
}

// ================= 파일 사이즈 체크 2 ====================
function weeklyFileCheck4(e){

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
            $("#imageView4").attr('src', '/image/uploadSample.png');
            return;
        }

        //========= 이미지 미리보기 기능 구현 =========
        sel_file = f;
        let reader = new FileReader();
        reader.onload = function(e) {
            $("#imageView4").attr("src", e.target.result);
            $("#imageView4").css("width", '150px');
        }
        reader.readAsDataURL(f);
    });
}



// ================= weekly Image Upload Process ====================
function weeklyImageProcess(csrfHeader, csrfToken) {

    imageObject = {};

    // Create : 이미지를 업로드하는 경우
    if( $("#image1").val() || $("#image2").val() || $("#image3").val() || $("#image4").val() ){
        var fileUploadCallback = ajaxFileUpload(csrfHeader, csrfToken, new FormData($("#weeklyFrm")[0]), "weekly");
        fileUploadCallback.done( uploadData => {
            if(uploadData.image1 != undefined){
                imageObject.image1_uploadName =  uploadData.image1.originalName;
                imageObject.image1_originalName =  uploadData.image1.uploadName;
            }
            if(uploadData.image2 != undefined){
                imageObject.image2_uploadName =  uploadData.image2.originalName;
                imageObject.image2_originalName =  uploadData.image2.uploadName;
            }
            if(uploadData.image3!= undefined){
                imageObject.image3_uploadName =  uploadData.image3.originalName;
                imageObject.image3_originalName =  uploadData.image3.uploadName;
            }
            if(uploadData.image4 != undefined){
                imageObject.image4_uploadName =  uploadData.image4.originalName;
                imageObject.image4_originalName =  uploadData.image4.uploadName;
            }
        });
    }


    // Update : 이미지를 수정하지 않는 경우
    if($("#image1").val() == "" && $("#image1_savedOriginalName").val()){
        imageObject.image1_uploadName  = $("#image1_savedUploadName").val();
        imageObject.image1_originalName  = $("#image1_savedOriginalName").val();
    }
    if($("#image2").val() == "" && $("#image2_savedOriginalName").val()){
        imageObject.image1_uploadName  = $("#image2_savedUploadName").val();
        imageObject.image1_originalName  = $("#image2_savedOriginalName").val();
    }
    if($("#image3").val() == "" && $("#image3_savedOriginalName").val()){
        imageObject.image3_uploadName  = $("#image3_savedUploadName").val();
        imageObject.image3_originalName  = $("#image3_savedOriginalName").val();
    }
    if($("#image4").val() == "" && $("#image2_savedOriginalName").val()){
        imageObject.image4_uploadName  = $("#image4_savedUploadName").val();
        imageObject.image4_originalName  = $("#image4_savedOriginalName").val();
    }
    return imageObject;
}

