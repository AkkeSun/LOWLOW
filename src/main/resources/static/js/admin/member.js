
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
