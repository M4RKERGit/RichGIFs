let xhr = new XMLHttpRequest();
xhr.open("GET", '/api/USD', true);
xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
xhr.send();

xhr.onreadystatechange = function()
{
    if (xhr.readyState === 4 && xhr.status === 200)
    {
        console.log(xhr.responseText)
        let jsonData = JSON.parse(xhr.responseText);
        showAll(jsonData);
    }
}

function showAll(jsonData)
{
    console.log(jsonData);
    output = "";
    output += jsonData.headerMsg + "\n";
    output += jsonData.firstCourse + "\n";
    output += jsonData.gifURL + "\n";
    document.getElementById('info').innerText = output;
    document.getElementById('resultGif').setAttribute('src', makeEmbed(jsonData.gifURL));
}

function makeEmbed(url)
{
    let splitted = url.toString().split("-");
    return "https://giphy.com/embed/" + splitted[splitted.length - 1];
}
