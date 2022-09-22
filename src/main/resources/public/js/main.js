function loadStuff() {
    document.getElementById("dynamic-stuff").innerText = "";

    const xhttp = new XMLHttpRequest();
    xhttp.onload = function () {
        createStuffListItem(JSON.parse(this.responseText), document.getElementById("dynamic-stuff"));
    }
    xhttp.open("GET", "/api/stuff", true);
    xhttp.send();
}

function createStuffListItem(stuff, parentList) {
    // display matching items
    const listItem = document.createElement("li");
    listItem.innerText = toString(stuff);
    parentList.append(listItem, createChildList(stuff.nestedStuff));
}

function toString(stuff) {
    const orEmptyString = obj => obj === undefined ? "" : obj;
    return `${stuff.name} - ${orEmptyString(stuff.description)} ${orEmptyString(stuff.labels)}`;
}

function createChildList(nestedStuff) {
    const list = document.createElement("ul");
    nestedStuff
        .filter(stuff => matchesSearch(stuff))
        .forEach(stuff => createStuffListItem(stuff, list));
    return list;
}

function matchesSearch(stuff) {
    const searchText = document.getElementById("search-box").value;
    return JSON.stringify(stuff).toLowerCase().includes(searchText.toLowerCase());
}
