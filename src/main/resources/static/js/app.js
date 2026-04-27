const API = "/api/admin/users";

// ================= LOGIN =================
async function login() {
    const res = await fetch("/api/auth/login", {
        method: "POST",
        credentials: "include",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            username: document.getElementById("username").value,
            password: document.getElementById("password").value
        })
    });

    if (!res.ok) {
        const msg = document.getElementById("msg");
        if (msg) msg.innerText = "Login failed";
        return;
    }

    const data = await res.json();

    if (data.roles.includes("ROLE_ADMIN")) {
        window.location.href = "/admin.html";
    } else {
        window.location.href = "/user.html";
    }
}

// ================= USERS =================
async function loadUsers() {
    const res = await fetch(API, {
        credentials: "include"
    });

    if (res.status === 401) {
        window.location.href = "/login";
        return;
    }

    if (!res.ok) {
        alert("Cannot load users");
        return;
    }

    const users = await res.json();
    const table = document.getElementById("usersTable");
    table.innerHTML = "";

    users.forEach(u => {
        table.innerHTML += `
            <tr>
                <td>${u.id}</td>
                <td>${u.username}</td>
                <td>${u.email}</td>
                <td>${u.roles.join(", ")}</td>
                <td><button onclick='editUser(${JSON.stringify(u)})'>Edit</button></td>
                <td><button onclick="deleteUser(${u.id})">Delete</button></td>
            </tr>
        `;
    });
}

// ================= ROLES =================
async function loadRoles() {
    const res = await fetch("/api/admin/users/roles", {
        credentials: "include"
    });

    if (!res.ok) {
        alert("Cannot load roles");
        return;
    }

    const roles = await res.json();

    const createSelect = document.getElementById("roles");
    const editSelect = document.getElementById("editRoles");

    createSelect.innerHTML = "";
    editSelect.innerHTML = "";

    roles.forEach(r => {
        const option1 = document.createElement("option");
        option1.value = r.id;
        option1.textContent = r.role;
        createSelect.appendChild(option1);

        const option2 = document.createElement("option");
        option2.value = r.id;
        option2.textContent = r.role;
        editSelect.appendChild(option2);
    });
}

// ================= CREATE =================
async function createUser() {
    const username = document.getElementById("username").value;
    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;

    const roleIds = Array.from(document.getElementById("roles").selectedOptions)
        .map(option => Number(option.value));

    const res = await fetch(API, {
        method: "POST",
        credentials: "include",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({ username, email, password, roleIds })
    });

    if (!res.ok) {
        alert("Create failed");
        return;
    }

    document.getElementById("username").value = "";
    document.getElementById("email").value = "";
    document.getElementById("password").value = "";

    loadUsers();
}

// ================= EDIT =================
function editUser(user) {
    document.getElementById("editId").value = user.id;
    document.getElementById("editUsername").value = user.username;
    document.getElementById("editEmail").value = user.email;
    document.getElementById("editPassword").value = "";

    const select = document.getElementById("editRoles");

    Array.from(select.options).forEach(option => {
        option.selected = user.roleIds.includes(Number(option.value));
    });
}

// ================= UPDATE =================
async function updateUser() {
    const id = document.getElementById("editId").value;
    const username = document.getElementById("editUsername").value;
    const email = document.getElementById("editEmail").value;
    const password = document.getElementById("editPassword").value;

    const roleIds = Array.from(document.getElementById("editRoles").selectedOptions)
        .map(option => Number(option.value));

    const res = await fetch(`${API}/${id}`, {
        method: "PUT",
        credentials: "include",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({ username, email, password, roleIds })
    });

    if (!res.ok) {
        alert("Update failed");
        return;
    }

    document.getElementById("editId").value = "";
    document.getElementById("editUsername").value = "";
    document.getElementById("editEmail").value = "";
    document.getElementById("editPassword").value = "";

    loadUsers();
}

// ================= DELETE =================
async function deleteUser(id) {
    const res = await fetch(`${API}/${id}`, {
        method: "DELETE",
        credentials: "include"
    });

    if (!res.ok) {
        alert("Delete failed");
        return;
    }

    loadUsers();
}

// ================= INIT =================
window.onload = async () => {
    if (document.getElementById("usersTable")) {
        await loadRoles();
        await loadUsers();
    }
};