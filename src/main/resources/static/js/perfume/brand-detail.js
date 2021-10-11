function save(id) {
    fetch("/api/v1/brand/" + id, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({
            name: document.getElementById("nameInput").value,
            koreanName: document.getElementById("koreanNameInput").value
        }),
    }).then((response) => console.log(response));
}