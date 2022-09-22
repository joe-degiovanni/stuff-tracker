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
    listItem.setAttribute("class", "list-group-item");
    listItem.innerText = toString(stuff)
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
