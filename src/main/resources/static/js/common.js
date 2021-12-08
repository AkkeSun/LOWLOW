//---------- 링크 이동 ---------
let link = (link) => {
    let appendContent = $("<span th:replace='admin/contents/test'></span>");
    $("#mainContent").empty();
    $("#mainContent").append(appendContent);

}




//---------- 링크 이동 ---------
let link2 = (link) => {
    let csrfHeader = $("#_csrf_header").attr('content');
    let csrfToken = $("#_csrf").attr('content');

    let callback =
        $.ajax({
            type       : "get",
            url        : link,
            beforeSend: function (xhr) {
                xhr.setRequestHeader("Content-type", "application/json");
                xhr.setRequestHeader(csrfHeader, csrfToken)
            }
        })
    callback.success( data => {
        // 데이터 처리 함수 -> 각각 만들기
        $("#mainContents").attr('th:replace', 'admin/contents/home');
    });
    callback.fail( (xhr, status, error) => {
        let errMsg = JSON.parse(xhr.responseText).message;
        if(xhr.status == '401') // 비로그인
            location.href = "/admin/login";
        if(xhr.status == '403') // 접근 권한 없는 사용자
            location.href = "/admin/denied?exception=" + errMsg;
    })
}
