# Getting the storefront

```
https://marketplace.visualstudio.com/_apis/public/gallery/extensionquery
```

- Should be of type `POST` 
- Should contain a header `Content-Type: application/json; charset=utf-8`
- Should contain a payload similar to this:

```json
{
    "assetTypes": [
        "Microsoft.VisualStudio.Services.Icons.Default",
        "Microsoft.VisualStudio.Services.Icons.Branding",
        "Microsoft.VisualStudio.Services.Icons.Small"
    ],
    "filters": [
        {
            "criteria": [
                {
                    "filterType": 8,
                    "value": "Microsoft.VisualStudio.Code"
                },
                {
                    "filterType": 10,
                    "value": "target:\"Microsoft.VisualStudio.Code\" "
                },
                {
                    "filterType": 12,
                    "value": "37888"
                },
                {
                    "filterType": 5,
                    "value": "Themes"
                }
            ],
            "direction": 2,
            "pageSize": 54,
            "pageNumber": 1,
            "sortBy": 4,
            "sortOrder": 0,
            "pagingToken": null
        }
    ],
    "flags": 870
}
```

You can use a command like this to retrieve a valid response:

```
curl -X POST https://marketplace.visualstudio.com/_apis/public/gallery/extensionquery --header "Content-Type: application/json; charset=utf-8" -d @microsoft-store-sample-payload.json
```
