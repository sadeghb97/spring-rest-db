var gridDemo = document.getElementById('ranked_list'),
    gridDemo2 = document.getElementById('unranked_list');

// Grid demo
new Sortable(gridDemo, {
    animation: 150,
    group: 'shared', // set both lists to same group
    ghostClass: 'blue-background-class'
});

new Sortable(gridDemo2, {
    group: 'shared', // set both lists to same group
    animation: 150,
    ghostClass: 'blue-background-class'
});
