<html>
<head>
    <title>The P.I.G</title>
    <script src="/assets/jquery-1.11.1.min.js"></script>

    <script type='text/javascript'>
        window.onload = function(){
//            log("called onload");

            if(typeof(EventSource) !== "undefined") {
                setupEventSource("/channel/events");
            } else {
                log("EventSource is undefined");
                addAlert("Sorry, your browser does not support server-sent events, so it cannot receive sound events. You can still play sounds to other users though. ");
            }

            if (typeof(Audio) == "undefined") {
                log("Audio is undefined");
                addAlert("Sorry, your browser does not support HTML5 Audio, so this page cannot play sounds.");
            }
        };

        function log(msg) {
            // stupid IE doesn't support console, so processing stops. annoy the user with alerts instead.
            if (typeof(console) !== "undefined") console.log(msg);
//            else alert(msg);
        }

        function addLineToElement(div, msg) {
            var msgElement = document.getElementById(div);
            msgElement.innerHTML = msgElement.innerHTML + msg + "<br/>";
        }

        function addAlert(msg) {
            addLineToElement("alerts", msg);
        }

        function addMessage(msg) {
            addLineToElement("messages", msg);
        }

        function setupEventSource(path) {
            var source = new EventSource(path);

            source.addEventListener('open', function(e) {
                // Connection was opened.
                log("EventListener connected to " + path);
            }, false);

            source.addEventListener('error', function(e) {
                console.log("error: " + e);
                switch (e.target.readyState) {
                    // if reconnecting
                    case EventSource.CONNECTING:
                        log("Error. Reconnecting...");
                        break;
                    // if error was fatal
                    case EventSource.CLOSED:
                        // Connection was closed.
                        log("Error. Connection closed.");
                        break;
                }
            }, false);

//            source.onmessage = function(event) {
//                log("onmessage default event handler: " + event.data);
//            };

            // default event is message
            source.addEventListener('message', function(event) {
                log("message event: " + event.data);
//                log("lastEventId: " + event.lastEventId);
                var data = jQuery.parseJSON(event.data);
                addMessage("message: " + data.message)
            }, false);

            source.addEventListener('sound', function(event) {
                log("sound event: " + event.data);
//                log("lastEventId: " + event.lastEventId);
                var data = jQuery.parseJSON(event.data);
                log("event data: " + data);
                playLocal(data.sound);
            }, false);
        }

        function squeeze() {
            addMessage("Generating Participation Interaction!");
            playAll("2squeaks");
        }

        function playAll(sound) {
            $.post('/channel/play', {sound: sound});
        }

        function playLocal(sound) {
            if (typeof(Audio) == "undefined") {
                addMessage("This browser cannot play sounds using HTML5 Audio.")
            } else {
                addMessage("Playing sound " + sound + "");
                var path = "/assets/sounds/" + sound + ".wav";
                log("playing " + path);
                var snd = new Audio(path); // buffers automatically when created
                snd.play();
            }
        }
    </script>
    <style>
        .centered {
            text-align: center;
        }
    </style>
</head>

<body>

<div class="centered">
    <h2>Participation Interaction Generator</h2><br/>

    <div id="alerts"></div>

    <h3>Squeeze the pig!</h3>

    <a href="#" onclick="squeeze(); return false;" ><img src="/assets/pig.png" border="0"/></a><br/>

    <br/>

    <!--<input type="button" name="sound" value="Play sound locally" onclick="playLocal('2squeaks'); return false;" /><br/>-->

    <br/>
    <div id="messages"></div>

</div>

</body>
</html>