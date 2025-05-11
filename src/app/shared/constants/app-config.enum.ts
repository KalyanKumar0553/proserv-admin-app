export const appConfig: any = {
    'configuration': {
        "sideNavMenu": [
            {
                "label": "home",
                "labelText": "Home",
                "active": false,
                "route": "home"
            },
            {
                "label": "configuration",
                "labelText": "Categories",
                "active": true,
                "children": [
                    { "label": "View Categories", "route": "list-categories", "active": true },
                    { "label": "Add / Update Category", "route": "add-update-category", "active": false },
                ]
            },
            {
                "label": "configuration",
                "labelText": "Providers",
                "active": false,
                "children": [
                    { "label": "Providers", "route": "list-providers", "active": true },
                ]
            },
            {
                "label": "configuration",
                "labelText": "Settings",
                "active": false,
                "route": "settings"
            }
        ]
    },
    'home': {
        "sideNavMenu": [
            {
                "label": "home",
                "labelText": "Home",
                "active": false,
                "route": "home"
            },
            {
                "label": "configuration",
                "labelText": "Configuration",
                "active": false,
                "route": "list-categories"
            }
        ]
    }
};