
// 주소값을 입력하면 위도 경도를 출력하는 함수 (Kakao Local)
function getAddressPath(address){

    var url = "https://dapi.kakao.com/v2/local/search/address.json?query="+address;
    var apiKEy = 'KakaoAK 13313c74ca7bcf1af98bec7853756162'; // KakaoAK 유저 RestAPI Key
    var addressPath = {};

    $.ajax({
        url:url,
        type:'GET',
        headers: {'Authorization' : apiKEy},
        success:function(data){
            addressPath.latitude = data.documents[0].address.y;   // 위도
            addressPath.longitude = data.documents[0].address.x;  // 경도
        },
        error : function(e){
            console.log(e);
        }
    });

    return addressPath;
}