/**************************************************************
 *
 *                       LIST FUNCTION
 *
 * accountingDataLoad(nowPage)         : 리스트 & 통계분석 데이터 통합로드
 * accountingListLoad(nowPage)         : 리스트 데이터 로드
 * accountingStatisticsDataLoad()      : 통계분석 데이터 로드
 * accountingPagingProcess()           : 페이징 함수
 * accountingSearch()                  : 검색 기능 함수
 * accountingSearchInitialize()        : 검색 초기화 함수
 * showStatisticsBtn()                 : 회계분석 Show 함수
 * showListBtn()                       : 전체 리스트 Show 함수
 * offeringKindConverter(offeringKind) : 헌금 종류 converter
 * nullConverter(data)                 : null converter
 *  accountingExelDown()               : 엑셀파일 다운로드 함수
 *
 **************************************************************/

let savedSearchId;    // 검색목록
let savedSearchData;  // 검색어
let startDate;        // 검색 시작일
let endDate;          // 검색 종료일

// ================= 리스트 & 회계분석 데이터 로드 ====================
function accountingDataLoad(nowPage){

    savedSearchId = $("#searchId").val();
    savedSearchData = $("#searchData").val();
    startDate = $("#startDate").val();
    endDate = $("#endDate").val();

    accountingListLoad(nowPage);
    accountingStatisticsDataLoad();
}



// ================= List Data Load  ====================
function accountingListLoad(nowPage){

    // param setting
    let type        = "get";
    let data        = "searchId="+savedSearchId+"&searchData="+savedSearchData+
                      "&startDate="+startDate+"&endDate="+endDate+
                      "&nowPage="+nowPage+"&totalPages=10";
    let url         = "/api/accounting";
    let csrfHeader  = $("#_csrf_header").attr('content');
    let csrfToken   = $("#_csrf").attr('content');
    let async       = false;
    let callback    = ajaxComm(type, data, url, async, csrfHeader, csrfToken);

    callback.done( (data) => {

        // global param setting
        totalPages = data.page.totalPages; // for paging

        let appendData = "";
        if( data._embedded ) {

            let accountingList = data._embedded.accountingList;

            accountingList.forEach(function (data, index) {
                let offeringKind = offeringKindConverter( data.offeringKind );
                let note         = nullConverter( data.note );
                let belong       = nullConverter (data.member.belong);
                let phoneNum     = nullConverter (data.member.phoneNumber);

                appendData += `
                           <tr id="appendItem">
                                <td style="text-align: left;">
                                    ${belong}
                                </td>
                                <td style="text-align: left;">
                                    <a href='/admin/accounting/${data.id}'>
                                        ${data.member.name}
                                    </a>
                                </td>
                                <td style="text-align: left;">
                                    ${phoneNum}
                                </td>
                                <td style="text-align: left;">
                                    ${data.money}
                                </td>
                                <td style="text-align: left;">
                                    ${offeringKind}
                                </td>
                                <td style="text-align: left;">
                                    ${data.offeringDate}
                                </td>
                                <td style="text-align: left;">
                                    ${note}
                                </td>
                            </tr>
                            `
            });
        }

        // 기존 데이터 삭제
        $("tr").remove("#appendItem");

        // data append
        $("#appendPath").append(appendData);
    });

    callback.fail ( (xhr, status, error) => {
        let errorResource = JSON.parse(xhr.responseText).content[0];
        console.log("[ERROR STATUS] : " + xhr.status);
        console.log(errorResource);
        alert(errorResource.defaultMessage);
    });

};



// ================= 통계 분석 데이터 로드 ====================
function accountingStatisticsDataLoad(){

    // param setting
    let type        = "get";
    let data        = "searchId="+savedSearchId+"&searchData="+savedSearchData+
                      "&startDate="+startDate+"&endDate="+endDate;
    let url         = "/api/accounting/statistics";
    let csrfHeader  = $("#_csrf_header").attr('content');
    let csrfToken   = $("#_csrf").attr('content');
    let async       = false;
    let callback    = ajaxComm(type, data, url, async, csrfHeader, csrfToken);

    callback.done( (data) => {

        let nameAppendData = "";
        let offeringKindAppendData = "";

        // 이름별 통계 데이터 
        if( data.member ) {
            let memberList = data.member;
            memberList.forEach(function (data, index) {
                nameAppendData += `
                                   <tr id="memberAppendItem">
                                        <td style="text-align: left;">
                                            ${data.name}
                                        </td>
                                        <td style="text-align: left;">
                                                ${data.money}
                                        </td>
                                    </tr>
                                    `
            });
        }

        // 헌금 종류별 통계 데이터
        if( data.offeringKind) {
            let offeringKindList = data.offeringKind;
            offeringKindList.forEach(function (data, index) {

                let offeringKind = offeringKindConverter( data.offeringKind );
                offeringKindAppendData += `
                                       <tr id="offeringKindAppendItem">
                                            <td style="text-align: left;">
                                                ${offeringKind}
                                            </td>
                                            <td style="text-align: left;">
                                                 ${data.money}
                                            </td>
                                        </tr>
                                        `
            });
        }

        // 기존 데이터 삭제
        $("tr").remove("#memberAppendItem");
        $("tr").remove("#offeringKindAppendItem");

        // data append
        $("#nameAppendPath").append(nameAppendData);
        $("#offeringKindAppendPath").append(offeringKindAppendData);
    });

    callback.fail ( (xhr, status, error) => {
        let errorResource = JSON.parse(xhr.responseText).content[0];
        console.log("[ERROR STATUS] : " + xhr.status);
        console.log(errorResource);
    });
}




// ================= paging process ====================
function accountingPagingProcess(){

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
            accountingListLoad(nowPage-1);
        }
    });
}


// ================ 검색 기능 ================
function accountingSearch(){
    accountingDataLoad(0);
    accountingPagingProcess();
}

// ================ 검색 초기화 ================
function accountingSearchInitialize() {
    $("#searchId").val("name");
    $("#searchData").val("");
    $("#startDate").val("");
    $("#endDate").val("");
    accountingDataLoad(0);
    accountingPagingProcess();
}

// ================ 회계분석 클릭 ================
function showStatisticsBtn(){
    $("#statisticsData").show();
    $("#statisticBtn").hide();
    $("#listBtn").show();
    $("#listData").hide();
}


// ================ 전체 리스트 버튼 클릭 ================
function showListBtn(){
    $("#statisticsData").hide();
    $("#statisticBtn").show();
    $("#listBtn").hide();
    $("#listData").show();
}


// ================= 액셀파일 다운로드 ====================
function accountingExelDown(){
    location.href = `/admin/accounting/excelDown?searchId=${savedSearchId}&searchData=${savedSearchData}&startDate=${startDate}&endDate=${endDate}`;
}


// ================= Null 인경우 '-' 리턴 ====================
function nullConverter(data){
    if(data == null) return "-";
    else return data;
}


// ================= 헌금종류 converter ====================
function offeringKindConverter(offeringKind){

    switch(offeringKind){
        case "SUNDAY"       : return "주정헌금";
        case "TITHE"        : return "십일조";
        case "THANKSGIVING" : return "감사헌금";
        case "BUILDING"     : return "건축헌금";
        case "SPECIAL"      : return "특별헌금";
        case "MISSION"      : return "선교헌금";
        case "UNKNOWN"      : return "기타헌금";
        case "TOTAL"        : return "TOTAL"  ;
    }
}






/**************************************************************
 *
 *                  CREATE & UPDATE FUNCTION
 *
 **************************************************************/
// ================= Create & Update process  ====================
function accountingCreateAndUpdateProcess(type){

    // param setting
    let csrfHeader  = $("#_csrf_header").attr('content');
    let csrfToken   = $("#_csrf").attr('content');
    let url         = "/api/accounting";
    let redirectUrl = "/admin/accounting";
    let async       = true;
    let isSecurity  = false;
    let data        = objToJson($('#accountingForm').serializeArray());

    // update url 변경
    if(type == 'put')
        url = "/api/members/"+$("#id").val();

    // ajax process
    let callback = ajaxComm(type, JSON.stringify(data), url, async, csrfHeader, csrfToken);

    callback.done( data => ajaxCallbackProcess(isSecurity, data, type, redirectUrl) );

    callback.fail ( (xhr, status, error) => {

        let errorResource = JSON.parse(xhr.responseText).content[0];
        console.log("[ERROR STATUS] : " + xhr.status);
        console.log(errorResource);
        alert(errorResource.defaultMessage);
    });

};

// ================= 헌금한 사람 이름 검색 함수 ====================
function nameCheckFunc(){

    // A. 이름을 입력하지 않은 경우
    if($("#nameCheck").val() == "") {
        alert('이름을 입력하지 않았습니다');
        $("#nameCheck").focus();
    }

    // B. "익명"을 입력한 경우
    else if($("#nameCheck").val() == "익명"){
        alert("확인 되었습니다");
        memberIdSetting(-1);


    // C. 그 외의 이름을 입력한 경우
    }else{

        let type        = "get";
        let data        = "searchId=name"+"&searchData="+$("#nameCheck").val()+"&nowPage=0";
        let url         = "/api/members";
        let csrfHeader  = $("#_csrf_header").attr('content');
        let csrfToken   = $("#_csrf").attr('content');
        let async       = false;
        let callback    = ajaxComm(type, data, url, async, csrfHeader, csrfToken);

        callback.done( (data) => {

            let appendData = "";

            // C-1. 일치하는 성도가 없는 경우
            if( !data._embedded ) {
                alert("일치하는 성도가 없습니다") ;
                $("#nameCheck").focus();

            // C-2. 일치하는 성도가 있는경우 모달창 출력하여 선택하도록 유도
            } else {

                let list = data._embedded.memberList;
                list.forEach(function (data, index) {
                    appendData += `
                               <tr id="appendItem">
                                    <td style="text-align: left;">
                                        ${data.belong}
                                    </td>
                                    <td style="text-align: left;">
                                         ${data.name}
                                    </td>
                                    <td style="text-align: left;">
                                        ${data.phoneNumber}
                                    </td>
                                    <td style="text-align: left;">
                                        ${data.birthDay}
                                    </td>
                                    <td style="text-align: left;">
                                        <button class="btn btn-outline-primary" onclick="memberIdSetting( ${data.id} )">선택</button>
                                    </td>
                                </tr>
                                `
                });
                $("tr").remove("#appendItem");            // 기존 데이터 삭제
                $("#modalAppendPath").append(appendData); // data append
                $('#memberCheckModal').modal('show');     // modal show
            }
        });
    }
}


// ================= 헌금한 사람 최종 셋팅 ====================
function memberIdSetting(id){

    $("#memberId").val(id);
    $('#memberCheckModal').modal('hide');
    $("#accountingNameUpdate").show();
    $("#accountingNameCheck").hide();
    $("#nameCheck").attr("disabled", "disabled");

}

// ================= 헌금한 사람 수정 버튼 ====================
function nameUpdateFunc(){
    $("#accountingNameUpdate").hide();
    $("#accountingNameCheck").show();
    $("#nameCheck").removeAttr("disabled");
}




// ================= Update View Setting ====================
function accountingUpdateViewSetting() {
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




// ================= Delete Process ====================
function accountingDelete(){

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

