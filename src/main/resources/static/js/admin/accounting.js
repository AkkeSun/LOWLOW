
// ================= Data Load  ====================
function accountingListLoad(nowPage){

    // param setting
    let type        = "get";
    let data        = "searchId="+$("#searchId").val()+"&searchData="+$("#searchData").val()+
                      "&startDate="+$("#startDate").val()+"&endDate="+$("#endDate").val()+
                      "&nowPage="+nowPage;
    let url         = "/api/accounting";
    let csrfHeader  = $("#_csrf_header").attr('content');
    let csrfToken   = $("#_csrf").attr('content');
    let async       = false;
    let callback    = ajaxComm(type, data, url, async, csrfHeader, csrfToken);

    callback.done( (data) => {

        // global param setting
        totalPages = data.page.totalPages; // for paging
        accountingNameCheck = false;       // for accounting create & update

        let appendData = "";
        if( data._embedded ) {

            let list = data._embedded.memberList;
            list.forEach(function (data, index) {
                appendData += `
                               <tr id="appendItem">
                                    <td style="text-align: left;">
                                        ${data.member.belong}
                                    </td>
                                    <td style="text-align: left;">
                                        <a href='/admin/members/${data.id}'>
                                            ${data.member.name}
                                        </a>
                                    </td>
                                    <td style="text-align: left;">
                                        ${data.member.phoneNumber}
                                    </td>
                                    <td style="text-align: left;">
                                        ${data.money}
                                    </td>
                                    <td style="text-align: left;">
                                        ${data.offeringKind}
                                    </td>
                                    <td style="text-align: left;">
                                        ${data.offeringDate}
                                    </td>
                                    <td style="text-align: left;">
                                        ${data.note}
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
            memberListLoad(nowPage-1);
        }
    });
}



// ================ List Search ================
function accountingSearch(){
    memberListLoad(0);
    memberPagingProcess();
}




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



// ================= 헌금한 사람 정보가 유효한지 체크 ====================
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
