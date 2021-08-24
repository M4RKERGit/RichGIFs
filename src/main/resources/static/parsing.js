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
        console.log(xhr.responseText)
        let jsonData = JSON.parse(xhr.responseText);
        showAll(jsonData);
    }
}

xhrAll.onreadystatechange = function()
{
    if (xhrAll.readyState === 4 && xhrAll.status === 200)
    {
        console.log(xhrAll.responseText);
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
