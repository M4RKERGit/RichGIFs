let xhr = new XMLHttpRequest();
callCurrency('RUB');

let xhrAll = new XMLHttpRequest();
xhrAll.open("GET", 'https://openexchangerates.org/api/currencies.json', true);
xhrAll.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
xhrAll.send();

document.getElementById('select').addEventListener('change', function()
{
    callCurrency(this.value);
})

xhr.onreadystatechange = function()
{
    if (xhr.readyState === 4 && xhr.status === 200)
    {
        let jsonData = JSON.parse(xhr.responseText);
        showAll(jsonData);
    }
}

xhrAll.onreadystatechange = function()
{
    if (xhrAll.readyState === 4 && xhrAll.status === 200)
    {
        let jsonData = JSON.parse(xhrAll.responseText);
        output = '';
        for (field in jsonData) output += '<option>' + field + '</option>';
        document.getElementById('select').innerHTML = output;
    }
}

function callCurrency(currency)
{
    xhr.open("GET", '/api/' + currency, true);
    xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    xhr.send();
}

function showAll(jsonData)
{
    document.getElementById('info').innerText = jsonData.headerMsg;
    document.getElementById('valueT').innerText = jsonData.course;
    document.getElementById('valueY').innerText = jsonData.courseYesterday;
    document.getElementById('resultGif').setAttribute('src', makeEmbed(jsonData.gifURL));
}

function makeEmbed(url)
{
    let splitted = '';
    if (url.toString().includes('-')) splitted = url.toString().split("-");
    else splitted = url.toString().split("/");
    return "https://giphy.com/embed/" + splitted[splitted.length - 1];
}