function loadJsonFile(jsonFileUrl, callback) {
    fetch(jsonFileUrl)
        .then(response => response.json())
        .then(callback)
        .catch(error => alert(`An error occurred while retrieving '${jsonFileUrl}'`));
}

function groupAutoValidationData(results) {
    return results.reduce((acc, item) => {
        const {category, criteria} = item;

        // First level: category
        acc[category] ??= {};

        // Second level: criteria
        acc[category][criteria] ??= [];

        // Add item
        acc[category][criteria].push(item);

        return acc;
    }, {});
}