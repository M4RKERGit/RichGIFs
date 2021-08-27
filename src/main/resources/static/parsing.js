let xhr = new XMLHttpRequest(); //запрос на наш API, по дефолту стоит рубль
callCurrency('RUB');

let xhrAll = new XMLHttpRequest();  //запрос для получения всего списка валют (быстрее сделать здесь, чем через запрос к нашему API)
xhrAll.open("GET", 'https://openexchangerates.org/api/currencies.json', true);  //и не нужно передавать ключ
xhrAll.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
xhrAll.send();

document.getElementById('select').addEventListener('change', function()
{
    callCurrency(this.value);   //чекаем изменения в <select> и вызываем справку по валюте при изменениях
})

xhr.onreadystatechange = function()
{
    if (xhr.readyState === 4 && xhr.status === 200)
    {
        let jsonData = JSON.parse(xhr.responseText);
        showAll(jsonData);  //распарщенные данные отдаем для обновления информации в вебе
    }
}

xhrAll.onreadystatechange = function()
{
    if (xhrAll.readyState === 4 && xhrAll.status === 200)
    {
        let jsonData = JSON.parse(xhrAll.responseText);
        output = '';
        for (field in jsonData) output += '<option>' + field + '</option>'; //из всего списка валют составляем HTMl-опции для <select>
        document.getElementById('select').innerHTML = output;   //и кидаем внутрь <select>
    }
}

function callCurrency(currency) //запрос к нашему API
{
    xhr.open("GET", '/api/' + currency, true);
    xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    xhr.send();
}

function showAll(jsonData)  //отображаем все данные из JSON в нашем вебе
{
    document.getElementById('info').innerText = jsonData.headerMsg;
    document.getElementById('valueT').innerText = jsonData.course;
    document.getElementById('valueY').innerText = jsonData.courseYesterday;
    document.getElementById('resultGif').setAttribute('src', makeEmbed(jsonData.gifURL));
}

function makeEmbed(url) //функция для превращения ссылки в embed, которую можно вставить себе на сайт
{
    let splitted = '';
    if (url.toString().includes('-')) splitted = url.toString().split("-");
    else splitted = url.toString().split("/");
    return "https://giphy.com/embed/" + splitted[splitted.length - 1];
}