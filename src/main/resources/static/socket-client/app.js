
function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}

function connect() {
    // var socket = new SockJS(
    //     '/gs-guide-websocket'
    // );
    // stompClient = Stomp.over(socket);
    // stompClient.connect({}, function (frame) {
    //     setConnected(true);
    //     console.log('Connected: ' + frame);
    //     stompClient.subscribe(
    //         '/topic/greetings',
    //         function (greeting) {
    //         showGreeting(JSON.parse(greeting.body).content);
    //     });
    // });
    ws = new WebSocket($("#hostName").val());
    ws.onmessage = function(data){
        showGreeting(data.data);
    }
    setConnected(true);
}

function disconnect() {
    // if (stompClient !== null) {
    //     stompClient.disconnect();
    // }
    // setConnected(false);
    // console.log("Disconnected");
    if (ws != null) {
        ws.close();
    }
    setConnected(false);
    console.log("Disconnected");
}

function sendName() {
    // stompClient.send("/app/hello", {}, JSON.stringify({'name': $("#name").val()}));
    //ws.send($("#name").val());
    ws.send($("#name").val());

}

function sendTextWithoutJson() {
    stompClient.send(
        "/app/message",
        {},
        //$("#textWithoutJson").val()
        JSON.stringify({'content': $("#textWithoutJson").val()})
    );
}

function showGreeting(message) {
    $("#greetings").append("<tr><td>" + message + "</td></tr>");
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#send" ).click(function() { sendName(); });
    $( "#sendTextWithoutJson" ).click(function() { sendTextWithoutJson(); })
});