

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
            list.forEach(function (data) {
                appendData += `
                               <tr id="appendItem">
                                    <td style="text-align: left;">
                                        ${data.id}
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



// ================= Weekly Detail Data ====================
function getWeeklyDetail(id) {

    let type        = "get";
    let url         = "/api/weekly/"+id;
    let csrfHeader  = $("#_csrf_header").attr('content');
    let csrfToken   = $("#_csrf").attr('content');
    let async       = true;
    let callback    = ajaxComm(type, null, url, async, csrfHeader, csrfToken);

    callback.done((data) => {
        $("#title").val(data.title);
        $("#weeklyDate").val(data.weeklyDate);
        $("#id").val(data.id);
        $("#imageDetailView").append(makeImageDetailView(data));
        $("#imageEditView").append(makeImageEditView(data));

        fileCheckProcess();
    });
}





// ================= make ImageDetail View ====================
function makeImageDetailView(data) {
    let HTML = "";

    if(data.img1)
    {
        HTML += `<div class="form-group">`;
        HTML += `<input type="hidden" id="image1_savedOriginalName" value="${data.img1.originalName}">`;
        HTML += `<input type="hidden" id="image1_savedUploadName"   value="${data.img1.uploadName}">`;
        HTML += `<input type="hidden" id="image1_savedFileDir"      value="${data.img1.fileDir}">`;
        HTML += `<input type="hidden" id="image1_savedFullUrl"      value="${data.img1.fullUrl}">`;
        if(data.img1.fileDir)
            HTML +=  `<img class="weeklyImg" id="img1" src="${data.img1.fullUrl}">`;
        else
            HTML +=  `<img class="weeklyImg" id="img1" src="/upload/weekly/${data.img1.uploadName}">`;
        HTML += `</div>`;
    }
    if(data.img2)
    {
        HTML += `<div class="form-group">`;
        HTML += `<input type="hidden" id="image2_savedOriginalName" value="${data.img2.originalName}">`;
        HTML += `<input type="hidden" id="image2_savedUploadName"   value="${data.img2.uploadName}">`;
        HTML += `<input type="hidden" id="image2_savedFileDir"      value="${data.img2.fileDir}">`;
        HTML += `<input type="hidden" id="image2_savedFullUrl"      value="${data.img2.fullUrl}">`;
        if(data.img2.fileDir)
            HTML +=  `<img class="weeklyImg" id="img2" src="${data.img2.fullUrl}">`;
        else
            HTML +=  `<img class="weeklyImg" id="img2" src="/upload/weekly/${data.img2.uploadName}">`;
        HTML += `</div>`;
    }
    if(data.img3)
    {
        HTML += `<div class="form-group">`;
        HTML += `<input type="hidden" id="image3_savedOriginalName" value="${data.img3.originalName}">`;
        HTML += `<input type="hidden" id="image3_savedUploadName"   value="${data.img3.uploadName}">`;
        HTML += `<input type="hidden" id="image3_savedFileDir"      value="${data.img3.fileDir}">`;
        HTML += `<input type="hidden" id="image3_savedFullUrl"      value="${data.img3.fullUrl}">`;
        if(data.img3.fileDir)
            HTML +=  `<img class="weeklyImg" id="img3" src="${data.img3.fullUrl}">`;
        else
            HTML +=  `<img class="weeklyImg" id="img3" src="/upload/weekly/${data.img3.uploadName}">`;
        HTML += `</div>`;
    }
    if(data.img4)
    {
        HTML += `<div class="form-group">`;
        HTML += `<input type="hidden" id="image4_savedOriginalName" value="${data.img4.originalName}">`;
        HTML += `<input type="hidden" id="image4_savedUploadName"   value="${data.img4.uploadName}">`;
        HTML += `<input type="hidden" id="image4_savedFileDir"      value="${data.img4.fileDir}">`;
        HTML += `<input type="hidden" id="image4_savedFullUrl"      value="${data.img4.fullUrl}">`;
        if (data.img4.fileDir)
            HTML += `<img class="weeklyImg" id="img4" src="${data.img4.fullUrl}">`;
        else
            HTML += `<img class="weeklyImg" id="img4" src="/upload/weekly/${data.img4.uploadName}">`;
        HTML += `</div>`;
    }

    return HTML;

}

// ================= make Edit View ====================
function makeImageEditView(data) {
    let HTML = "";

    // ~~~~~~~~~~~~~~~ image 1 ~~~~~~~~~~~~~~~~
    HTML += `<div class="form-group">`;
    HTML += `<input type="file" class="form-control-file input-large" id="image1" name="image1" style="display: none"> &nbsp;`;
    HTML += `<div>`;
    HTML += `<label for="image1">&nbsp;&nbsp;`;
    if(data.img1)
    {
        if(data.img1.fileDir)
            HTML += `<img class="weeklyImg" id="imageView1" src="${data.img1.fullUrl}">`;
        else
            HTML += `<img class="weeklyImg" id="imageView1" src="/upload/weekly/${data.img1.uploadName}">`;
    }
    else
        HTML += `<img class="imgSample" id="imageView1" src="/image/uploadSample.png">`;
    HTML += `</label>`;
    HTML += `</div>`;
    HTML += `</div>`;


    // ~~~~~~~~~~~~~~~ image 2 ~~~~~~~~~~~~~~~~
    HTML += `<div class="form-group">`;
    HTML += `<input type="file" class="form-control-file input-large" id="image2" name="image2" style="display: none"> &nbsp;`;
    HTML += `<div>`;
    HTML += `<label for="image2">&nbsp;&nbsp;`;
    if(data.img2)
    {
        if(data.img2.fileDir)
            HTML += `<img class="weeklyImg" id="imageView2" src="${data.img2.fullUrl}">`;
        else
            HTML += `<img class="weeklyImg" id="imageView2" src="/upload/weekly/${data.img2.uploadName}">`;
    }
    else
        HTML += `<img class="imgSample" id="imageView2" src="/image/uploadSample.png">`;
    HTML += `        </label>`;
    HTML += `    </div>`;
    HTML += `</div>`;


    // ~~~~~~~~~~~~~~~ image 3 ~~~~~~~~~~~~~~~~
    HTML += `<div class="form-group">`;
    HTML += `<input type="file" class="form-control-file input-large" id="image3" name="image3" style="display: none"> &nbsp;`;
    HTML += `<div>`;
    HTML += `<label for="image3">&nbsp;&nbsp;`;
    if(data.img3)
    {
        if(data.img3.fileDir)
            HTML += `<img class="weeklyImg" id="imageView3" src="${data.img3.fullUrl}">`;
        else
            HTML += `<img class="weeklyImg" id="imageView3" src="/upload/weekly/${data.img3.uploadName}">`;
    }
    else
        HTML += `<img class="imgSample" id="imageView3" src="/image/uploadSample.png">`;
    HTML += `</label>`;
    HTML += `</div>`;
    HTML += `</div>`;


    // ~~~~~~~~~~~~~~~ image 4 ~~~~~~~~~~~~~~~~
    HTML += `<div class="form-group">`;
    HTML += `<input type="file" class="form-control-file input-large" id="image4" name="image4" style="display: none"> &nbsp;`;
    HTML += `<div>`;
    HTML += `<label for="image4">&nbsp;&nbsp;`;
    if(data.img4)
    {
        if(data.img4.fileDir)
            HTML += `<img class="weeklyImg" id="imageView4" src="${data.img4.fullUrl}">`;
        else
            HTML += `<img class="weeklyImg" id="imageView4" src="/upload/weekly/${data.img4.uploadName}">`;
    }
    else
        HTML += `<img class="imgSample" id="imageView4" src="/image/uploadSample.png">`;
    HTML += `</label>`;
    HTML += `</div>`;
    HTML += `</div>`;

    return HTML;
}





// ================= Weekly Update View Setting ====================
function weeklyUpdateViewSetting() {
    $("#title").removeAttr("disabled");
    $("#weeklyDate").removeAttr("disabled");
    $("#imageEditView").removeClass('hide');
    $("#imageDetailView").addClass('hide');
};




// ================= 파일 사이즈 체크 ====================
function fileCheckProcess () {
    $("#image1").on("change", weeklyFileCheck1);
    $("#image2").on("change", weeklyFileCheck2);
    $("#image3").on("change", weeklyFileCheck3);
    $("#image4").on("change", weeklyFileCheck4);
}


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

    var imageObject = {image1:{}, image2:{}, image3:{}, image4:{}};

    // Create : 이미지를 업로드하는 경우
    if( $("#image1").val() || $("#image2").val() || $("#image3").val() || $("#image4").val() )
    {
        var fileUploadCallback = ajaxFileUpload(csrfHeader, csrfToken, new FormData($("#weeklyFrm")[0]), "weekly");
        fileUploadCallback.done( uploadData =>
        {
            if(uploadData.image1 != undefined)
                imageObject.image1 = uploadData.image1;
            if(uploadData.image2 != undefined)
                imageObject.image2 = uploadData.image2;
            if(uploadData.image3 != undefined)
                imageObject.image3 = uploadData.image3;
            if(uploadData.image4 != undefined)
                imageObject.image4 = uploadData.image4;
        });
    }

    // Update : 이미지를 수정하지 않는 경우
    if($("#image1").val() == "" && $("#image1_savedOriginalName").val()){
        imageObject.image1.uploadName   = $("#image1_savedUploadName").val();
        imageObject.image1.originalName = $("#image1_savedOriginalName").val();
        imageObject.image1.fileDir      = $("#image1_savedFileDir").val();
        imageObject.image1.fullUrl      = $("#image1_savedFullUrl").val();
    }
    if($("#image2").val() == "" && $("#image2_savedOriginalName").val()){
        imageObject.image2.uploadName   = $("#image2_savedUploadName").val();
        imageObject.image2.originalName = $("#image2_savedOriginalName").val();
        imageObject.image2.fileDir      = $("#image2_savedFileDir").val();
        imageObject.image2.fullUrl      = $("#image1_savedFullUrl").val();
    }
    if($("#image3").val() == "" && $("#image3_savedOriginalName").val()){
        imageObject.image3.uploadName   = $("#image3_savedUploadName").val();
        imageObject.image3.originalName = $("#image3_savedOriginalName").val();
        imageObject.image3.fileDir      = $("#image3_savedFileDir").val();
        imageObject.image3.fullUrl      = $("#image1_savedFullUrl").val();
    }
    if($("#image4").val() == "" && $("#image2_savedOriginalName").val()){
        imageObject.image4.uploadName   = $("#image4_savedUploadName").val();
        imageObject.image4.originalName = $("#image4_savedOriginalName").val();
        imageObject.image4.fileDir      = $("#image4_savedFileDir").val();
        imageObject.image4.fullUrl      = $("#image1_savedFullUrl").val();
        
    }


    return imageObject;
}

