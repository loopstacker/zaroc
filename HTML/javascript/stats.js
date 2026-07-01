let tbody = null;
let loadingNote = null;
let filterValueElement = null;
let sortValue = "player";
let sortStatus = "↑"

// we store the default values of the columns
let tableHeaderData = [
    {DOMHeadElement: document.getElementById("player-table-head"), tableHeadText: "PLAYER", jsonValue: "player"},
    {DOMHeadElement: document.getElementById("game-table-head"), tableHeadText: "GAME", jsonValue: "game"},
    {DOMHeadElement: document.getElementById("outcome-table-head"), tableHeadText: "OUTCOME", jsonValue: "outcome"},
    {
        DOMHeadElement: document.getElementById("duration-table-head"),
        tableHeadText: "DURATION (S)",
        jsonValue: "duration"
    },
    {DOMHeadElement: document.getElementById("outlier-table-head"), tableHeadText: "OUTLIER", jsonValue: "outlier"},
]

function setSortValue(DOMHeadElement) {
    let rowData = tableHeaderData.find(
        item => item.DOMHeadElement === DOMHeadElement
    );

    resetTableHeaders();
    sortStatus = sortStatus === "↑" ? "↓" : "↑";
    DOMHeadElement.innerText = rowData.tableHeadText + " " + sortStatus;

    sortValue = rowData.jsonValue;

    reRender();
}

function hideLoadingNote() {
    loadingNote.style.display = "none";
}

function fetchStats(stats) {
    let moves = [];
    const filterValue = filterValueElement.value.trim().toLowerCase();

    for (const item of stats) {
        const player = String(item.player).toLowerCase();
        const game = String(item.game).toLowerCase();
        if (!player.includes(filterValue) && !game.includes(filterValue)) continue;

        moves.push({
            player: item.player,
            game: item.game,
            outcome: item.outcome,
            duration: item.duration,
            outlier: item.outlier
        });
    }

    moves.sort((a, b) => {
        const va = a[sortValue];
        const vb = b[sortValue];

        let result;
        if (typeof va === "number" && typeof vb === "number") {
            result = va - vb;
        } else {
            result = String(va).localeCompare(String(vb));
        }

        return sortStatus === "↓" ? -result : result;
    });

    displayMoves(moves);
}

function displayMoves(moves) {
    tbody.innerHTML = "";
    for (const move of moves) {
        const table_row = document.createElement("tr");

        const cell_player = document.createElement("td");
        cell_player.textContent = move.player;

        const cell_game = document.createElement("td");
        cell_game.textContent = move.game;

        const cell_outcome = document.createElement("td");
        cell_outcome.textContent = move.outcome;

        const cell_duration = document.createElement("td");
        cell_duration.textContent = move.duration;

        const cell_outlier = document.createElement("td");
        cell_outlier.textContent = move.outlier;
        if (move.outlier === "X") {
            table_row.style.backgroundColor = "#b04813";
        }

        table_row.append(cell_player, cell_game, cell_outcome, cell_duration, cell_outlier);
        tbody.appendChild(table_row);
    }
    hideLoadingNote();
}

function refreshTheTable() {
    filterValueElement.value = "";
    sortValue = "player";
    sortStatus = "↑";
    resetTableHeaders();
    reRender();
}

function resetTableHeaders() {
    for (const col of tableHeaderData) {
        col.DOMHeadElement.innerText = col.tableHeadText;
    }
}

function reRender() {
    loadJsonFile('../game-data/moves.json', fetchStats);
}


function main() {
    loadJsonFile('../game-data/moves.json', fetchStats);
}

function init() {
    tbody = document.getElementById("moves-tbody");
    loadingNote = document.getElementById("leaderboard-loading-note");
    filterValueElement = document.getElementById("filter-value");
    main();
    document.getElementById("find-button").onclick = main;
    document.getElementById("refresh-button").onclick = refreshTheTable;
    for (const row of tableHeaderData) {
        row.DOMHeadElement.addEventListener("click", () => setSortValue(row.DOMHeadElement));
    }
}

init();
