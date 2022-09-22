let allStuff = undefined;

const plusSign = "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"16\" height=\"16\" fill=\"currentColor\" class=\"bi bi-plus-circle\" viewBox=\"0 0 16 16\">\n" +
    "  <path d=\"M8 15A7 7 0 1 1 8 1a7 7 0 0 1 0 14zm0 1A8 8 0 1 0 8 0a8 8 0 0 0 0 16z\"/>\n" +
    "  <path d=\"M8 4a.5.5 0 0 1 .5.5v3h3a.5.5 0 0 1 0 1h-3v3a.5.5 0 0 1-1 0v-3h-3a.5.5 0 0 1 0-1h3v-3A.5.5 0 0 1 8 4z\"/>\n" +
    "</svg>";

const trashIcon = "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"16\" height=\"16\" fill=\"currentColor\" class=\"bi bi-trash\" viewBox=\"0 0 16 16\">\n" +
    "  <path d=\"M5.5 5.5A.5.5 0 0 1 6 6v6a.5.5 0 0 1-1 0V6a.5.5 0 0 1 .5-.5zm2.5 0a.5.5 0 0 1 .5.5v6a.5.5 0 0 1-1 0V6a.5.5 0 0 1 .5-.5zm3 .5a.5.5 0 0 0-1 0v6a.5.5 0 0 0 1 0V6z\"/>\n" +
    "  <path fill-rule=\"evenodd\" d=\"M14.5 3a1 1 0 0 1-1 1H13v9a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V4h-.5a1 1 0 0 1-1-1V2a1 1 0 0 1 1-1H6a1 1 0 0 1 1-1h2a1 1 0 0 1 1 1h3.5a1 1 0 0 1 1 1v1zM4.118 4 4 4.059V13a1 1 0 0 0 1 1h6a1 1 0 0 0 1-1V4.059L11.882 4H4.118zM2.5 3V2h11v1h-11z\"/>\n" +
    "</svg>";

function reloadStuff() {
    allStuff = undefined;
    loadStuff();
}

function loadStuff() {
    document.getElementById("dynamic-stuff").innerText = "";

    if (allStuff === undefined) {
        const xhttp = new XMLHttpRequest();
        xhttp.onload = function () {
            allStuff = JSON.parse(this.responseText);
            createStuffListItem(allStuff, document.getElementById("dynamic-stuff"));
        }
        xhttp.open("GET", "/api/stuff", true);
        xhttp.send();
    } else {
        createStuffListItem(allStuff, document.getElementById("dynamic-stuff"));
    }
}

function saveStuff() {
    const xhttp = new XMLHttpRequest();
    xhttp.open("POST", "/api/stuff", false);
    xhttp.send(JSON.stringify(allStuff));
    reloadStuff();
}

function createStuffListItem(stuff, parentList) {
    // display matching items
    const listItem = document.createElement("li");
    listItem.setAttribute("class", "list-group-item");
    listItem.innerText = toString(stuff);
    listItem.appendChild(createSpanSpace());
    listItem.appendChild(createPlusButton());
    listItem.appendChild(createSpanSpace());
    listItem.appendChild(createTrashButton());
    listItem.appendChild(createChildList(stuff.nestedStuff));
    parentList.append(listItem);
}

function toString(stuff) {
    const orEmptyString = obj => obj === undefined ? "" : obj;
    return `${stuff.name} - ${orEmptyString(stuff.description)} ${orEmptyString(stuff.labels)}`;
}

function createChildList(nestedStuff) {
    const list = document.createElement("ul");
    list.setAttribute("class", "list-group list-group-flush");
    nestedStuff
        .filter(stuff => matchesSearch(stuff))
        .forEach(stuff => createStuffListItem(stuff, list));
    return list;
}

function matchesSearch(stuff) {
    const searchText = document.getElementById("search-box").value;
    return JSON.stringify(stuff).toLowerCase().includes(searchText.toLowerCase());
}

function createPlusButton() {
    const button = document.createElement("i");
    button.setAttribute("class", "bi bi-plus-circle");
    button.innerHTML = plusSign;
    button.setAttribute("onclick", "addRowBelow(this.parentElement)");
    return button;
}

function createTrashButton() {
    const button = document.createElement("i");
    button.setAttribute("class", "bi bi-trash");
    button.innerHTML = trashIcon;
    button.setAttribute("onclick", "console.log('delete me sometime')");
    return button;
}

function addRowBelow(element) {
    let arr = Array.from(element.childNodes).filter(it => it.tagName === "UL");
    if (arr.length > 0) {
        console.log(allStuff);
        allStuff.nestedStuff.push({name: "new item", nestedStuff: []});
        saveStuff();
    }
}

function createSpanSpace() {
    const spacer = document.createElement("span");
    spacer.innerText = " ";
    return spacer;
}
