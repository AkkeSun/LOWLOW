
function goBasicInfo() {

    let type        = "get";
    let data        = "";
    let url         = REST_API_URL + "/basicInfo/list";
    let csrfHeader  = $("#_csrf_header").attr('content');
    let csrfToken   = $("#_csrf").attr('content');
    let async       = false;
    let callback    = ajaxComm(type, data, url, async, csrfHeader, csrfToken);

    callback.done( data => {
        if(data.basicInfoList.length == 0) {
            e1Exception();
        } else {
            localStorage.clear();
            localStorage.setItem('basicInfo', JSON.stringify(data.basicInfoList[0]));
            localStorage.setItem('isUpdate', 'true');
            location.href = "/admin/basicInfo/1";
        }
    })
}



// set View ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
function setBasicInfoView1 () {

    let isUpdate = localStorage.isUpdate;
    let view1Save = localStorage.basicInfoView1Save;

    if (isUpdate == 'true' && view1Save != 'true') {
        let basicInfoData = JSON.parse(localStorage.basicInfo);
        $("#id").val(basicInfoData.id);
        $("#name").val(basicInfoData.name);
        $("#senior_pastor_name").val(basicInfoData.senior_pastor_name);
        $("#callNumber").val(basicInfoData.callNumber);
        $("#basicAddress").val(basicInfoData.basicAddress);
        $("#detailAddress").val(basicInfoData.detailAddress);
        $("#youtubeURL").val(basicInfoData.youtubeURL);
        $("#kakaoPage").val(basicInfoData.kakaoPage);
        $("#instagram").val(basicInfoData.instagram);
        $("#blog").val(basicInfoData.blog);
    }
    else if ( view1Save == 'true' )
    {
        let basicInfoData = JSON.parse(localStorage.newBasicInfo);
        $("#id").val(basicInfoData.id);
        $("#name").val(basicInfoData.name);
        $("#senior_pastor_name").val(basicInfoData.senior_pastor_name);
        $("#callNumber").val(basicInfoData.callNumber);
        $("#basicAddress").val(basicInfoData.basicAddress);
        $("#detailAddress").val(basicInfoData.detailAddress);
        $("#youtubeURL").val(basicInfoData.youtubeURL);
        $("#kakaoPage").val(basicInfoData.kakaoPage);
        $("#instagram").val(basicInfoData.instagram);
        $("#blog").val(basicInfoData.blog);
    }
}

function setBasicInfoView2 () {

    let isUpdate = localStorage.isUpdate;
    let view2Save = localStorage.basicInfoView2Save;

    if (isUpdate == 'true' && view2Save != 'true') {
        let basicInfoData = JSON.parse(localStorage.basicInfo);
        $("#basicInfo").val(basicInfoData.basicInfo);
        $('#contents').summernote('code', basicInfoData.detailInfo);
    }
    else if ( view2Save == 'true' )
    {
        let basicInfoData = JSON.parse(localStorage.newBasicInfo);
        $("#basicInfo").val(basicInfoData.basicInfo);
        $('#contents').summernote('code', basicInfoData.contents);
    }
}

function setBasicInfoView3 () {

    let isUpdate = localStorage.isUpdate
    if (isUpdate == 'true') {
        $("#updateBtn").removeClass("hide");
        $("#goAdminBtn").removeClass("hide");
        $("#saveBtn").addClass("hide");
    }
}




// validation ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
function view1Validation () {
    let result = true;
    if($("#name").val() == "") {
        $("#nameLabel1").removeClass("hide");
        $("#nameLabel2").addClass("hide");
        result = false;
    }
    else {
        $("#nameLabel2").removeClass("hide");
        $("#nameLabel1").addClass("hide");
    }
    if($("#senior_pastor_name").val() == "") {
        $("#senior_pastor_name_Label1").removeClass("hide");
        $("#senior_pastor_name_Label2").addClass("hide");
        result = false;
    }
    else {
        $("#senior_pastor_name_Label2").removeClass("hide");
        $("#senior_pastor_name_Label1").addClass("hide");
    }
    if($("#callNumber").val() == "") {
        $("#callNumberLabel1").removeClass("hide");
        $("#callNumberLabel2").addClass("hide");
        result = false;
    }
    else {
        $("#callNumberLabel2").removeClass("hide");
        $("#callNumberLabel1").addClass("hide");
    }
    if($("#basicAddress").val() == "") {
        $("#basicAddressLabel1").removeClass("hide");
        $("#basicAddressLabel2").addClass("hide");
        result = false;
    }
    else {
        $("#basicAddressLabel2").removeClass("hide");
        $("#basicAddressLabel1").addClass("hide");
    }
    return result;
}

function view2Validation () {
    let result = true;

    if($("#basicInfo").val() == "") {
        $("#basicInfoLabel1").removeClass("hide");
        $("#basicInfoLabel2").addClass("hide");
        result = false;
    }
    else {
        $("#basicInfoLabel2").removeClass("hide");
        $("#basicInfoLabel1").addClass("hide");
    }
    if($("#contents").val() == "") {
        $("#detailInfoLabel1").removeClass("hide");
        $("#detailInfoLabel2").addClass("hide");
        result = false;
    }
    else {
        $("#detailInfoLabel2").removeClass("hide");
        $("#detailInfoLabel1").addClass("hide");
    }
    return result;
}



// move Method ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
function goView1 (){
    if( view2Validation() ) {
        let newBasicInfo = JSON.parse(localStorage.newBasicInfo);
        newBasicInfo.basicInfo = $("#basicInfo").val();
        newBasicInfo.contents =  $("#contents").val();

        localStorage.setItem("newBasicInfo", JSON.stringify(newBasicInfo));
        localStorage.setItem("basicInfoView2Save", "true");
        location.href = "/admin/basicInfo/1";
    }
}

function goView2(prePage){

    if(prePage == 3)
    {
        let crm = confirm("이전 페이지로 이동하면 모든 파일이 삭제됩니다. 이동하시겠습니까")
        if(crm)
            location.href="/admin/basicInfo/2";
    }
    else if (prePage == 1)
    {
        if(view1Validation ()) {

            if(localStorage.newBasicInfo)
            {
                let newBasicInfo = JSON.parse(localStorage.newBasicInfo);
                newBasicInfo.id = $("#id").val();
                newBasicInfo.name = $("#name").val();
                newBasicInfo.senior_pastor_name = $("#senior_pastor_name").val();
                newBasicInfo.callNumber = $("#callNumber").val();
                newBasicInfo.basicAddress = $("#basicAddress").val();
                newBasicInfo.detailAddress = $("#detailAddress").val();
                newBasicInfo.youtubeURL = $("#youtubeURL").val();
                newBasicInfo.kakaoPage = $("#kakaoPage").val();
                newBasicInfo.blog = $("#blog").val();
                newBasicInfo.basicAddress = $("#basicAddress").val();
                localStorage.setItem("newBasicInfo", JSON.stringify(newBasicInfo));
            }
            else
            {
                let newBasicInfo = {
                    id : $("#id").val(),
                    name :  $("#name").val(),
                    senior_pastor_name :  $("#senior_pastor_name").val(),
                    callNumber : $("#callNumber").val(),
                    basicAddress :  $("#basicAddress").val(),
                    detailAddress :  $("#detailAddress").val(),
                    youtubeURL :  $("#youtubeURL").val(),
                    kakaoPage :  $("#kakaoPage").val(),
                    blog :  $("#blog").val(),
                    basicAddress :  $("#basicAddress").val(),
                };
                localStorage.setItem("newBasicInfo", JSON.stringify(newBasicInfo));
            }
            localStorage.setItem("basicInfoView1Save", "true");
            location.href = "/admin/basicInfo/2";
        }
    }
}

function goView3(){

    if(view2Validation ()) {
        let newBasicInfo = JSON.parse(localStorage.newBasicInfo);
        newBasicInfo.basicInfo = $("#basicInfo").val();
        newBasicInfo.contents = $("#contents").val();

        localStorage.setItem("newBasicInfo", JSON.stringify(newBasicInfo));
        localStorage.setItem("basicInfoView2Save", "true");
        location.href = "/admin/basicInfo/3";
    }
}

function goIndex() {
    localStorage.clear();
    location.href="/admin";
}



// create & update process ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
function basicInfoProcess (type) {

    let cfm = confirm("저장하시겠습니까");
    if(cfm) {

        let csrfHeader  = $("#_csrf_header").attr('content');
        let csrfToken   = $("#_csrf").attr('content');
        let url = '/api/basicInfo'

        // file Upload
        let fileUploadCallback = ajaxFileUpload(csrfHeader, csrfToken,  new FormData($("#basicInfoFrm3")[0]), "basicInfo");
        fileUploadCallback.done(data => {

            let saveData = setBasicInfoImg(type, data, JSON.parse(localStorage.newBasicInfo));
            saveData.detailInfo = saveData.contents;

            if(type == 'put')
                url = '/api/basicInfo/' + saveData.id;

            let callback = ajaxComm(type, JSON.stringify(saveData), url, true, csrfHeader, csrfToken);
            callback.done( () => {
                alert("성공적으로 등록되었습니다");
                goBasicInfo();
            });
            callback.fail( (err) => {
                console.log(err);
                alert("등록 중 에러가 발생하였습니다.");
                localStorage.clear();
                location.href="/admin";
            });
        });
    }
}






// ================= 주소 검색 함수 ====================
function addressSearch() {
    new daum.Postcode({
        oncomplete: function(data) {

            var addr = '';

            // 도로명 주소 (R)
            if (data.userSelectedType === 'R') {
                addr = data.roadAddress;
                // 지번주소 (J)
            } else {
                addr = data.jibunAddress;
            }
            $("#basicAddress").val(addr);
            $("#detailAddress").focus();
        }
    }).open();
}


function setBasicInfoImg (type, data, newBasicInfoData) {

    newBasicInfoData.infoImage1 = data.infoImg1;
    newBasicInfoData.infoImage2 = data.infoImg2;
    newBasicInfoData.infoImage3 = data.infoImg3;
    newBasicInfoData.infoImage4 = data.infoImg4;
    newBasicInfoData.infoImage5 = data.infoImg5;
    newBasicInfoData.infoImage6 = data.infoImg6;
    newBasicInfoData.carouselImg1 = data.carImg1;
    newBasicInfoData.carouselImg2 = data.carImg2;
    newBasicInfoData.carouselImg3 = data.carImg3;
    newBasicInfoData.carouselImg4 = data.carImg4;
    newBasicInfoData.carouselImg5 = data.carImg5;
    newBasicInfoData.carouselImg6 = data.carImg6;
    newBasicInfoData.organizationChart1 = data.chartImg1;
    newBasicInfoData.organizationChart2 = data.chartImg2;
    newBasicInfoData.organizationChart3 = data.chartImg3;

    if(type == 'put') {
        let preData = JSON.parse(localStorage.basicInfo);

        if (!data.infoImg1)
            newBasicInfoData.infoImage1 = preData.infoImage1;
        if (!data.infoImg2)
            newBasicInfoData.infoImage2 = preData.infoImage2;
        if (!data.infoImage3)
            newBasicInfoData.infoImage3 = preData.infoImage3;
        if (!data.infoImage4)
            newBasicInfoData.infoImage4 = preData.infoImage4;
        if (!data.infoImage5)
            newBasicInfoData.infoImage5 = preData.infoImage5;
        if (!data.infoImage6)
            newBasicInfoData.infoImage6 = preData.infoImage6;
        if (!data.carImg1)
            newBasicInfoData.carouselImg1 = preData.carouselImg1;
        if (!data.carImg2)
            newBasicInfoData.carouselImg2 = preData.carouselImg2;
        if (!data.carImg3)
            newBasicInfoData.carouselImg3 = preData.carouselImg3;
        if (!data.carImg4)
            newBasicInfoData.carouselImg4 = preData.carouselImg4;
        if (!data.carImg5)
            newBasicInfoData.carouselImg5 = preData.carouselImg5;
        if (!data.carImg6)
            newBasicInfoData.carouselImg6 = preData.carouselImg6;
        if (!data.chartImg1)
            newBasicInfoData.organizationChart1 = preData.organizationChart1;
        if (!data.chartImg2)
            newBasicInfoData.organizationChart2 = preData.organizationChart2;
        if (!data.chartImg3)
            newBasicInfoData.organizationChart3 = preData.organizationChart3;
    }

    return newBasicInfoData;
}


function e1Exception() {
    let check = confirm('등록된 교회정보가 없습니다. 신규 등록하시겠습니까?');
    if(check){
        location.href="/admin/basicInfo/1";
    }else{
        location.href="/admin";
    }
}
