# Additional Validation Constraints

The following validation rules are enforced for both `Item` and `Recipe` objects.

---

# Item Validation Rules

## 1. Item Name Must Not Be Blank

The `name` field is required and must contain at least one non-whitespace character.

### Valid

```json
{
  "name": "Iron Plate"
}
```

### Invalid

```json
{
  "name": ""
}
```

```json
{
  "name": "   "
}
```

---

# Recipe Validation Rules

## 1. Recipe Name Must Not Be Blank

The `name` field is required and must contain at least one non-whitespace character.

### Valid

```json
{
  "name": "Iron Smelting"
}
```

### Invalid

```json
{
  "name": ""
}
```

```json
{
  "name": "   "
}
```

---

## 2. `inputMaterials` Must Not Be Empty

A recipe must define at least one input material.

### Valid

```json
"inputMaterials": {
  "Iron Ore": 1.0
}
```

### Invalid

```json
"inputMaterials": {}
```

---

## 3. `outputMaterials` Must Not Be Empty

A recipe must define at least one output material.

### Valid

```json
"outputMaterials": {
  "Iron Ingot": 1.0
}
```

### Invalid

```json
"outputMaterials": {}
```

---

## 4. Material Amounts Must Be Greater Than Zero

All material quantities in both `inputMaterials` and `outputMaterials` must be positive numbers greater than `0`.

### Valid

```json
"inputMaterials": {
  "Iron Ore": 1.0
}
```

### Invalid

```json
"inputMaterials": {
  "Iron Ore": 0.0
}
```

```json
"inputMaterials": {
  "Iron Ore": -2.0
}
```

---

## 5. `primaryOutput` Must Exist in `outputMaterials`

The value of `primaryOutput` must exactly match one of the keys defined in `outputMaterials`.

Matching is case-sensitive.

### Valid

```json
"outputMaterials": {
  "Iron Plate": 1.0
},
"primaryOutput": "Iron Plate"
```

### Invalid

```json
"outputMaterials": {
  "Iron Plate": 1.0
},
"primaryOutput": "iron plate"
```

```json
"outputMaterials": {
  "Iron Rod": 1.0
},
"primaryOutput": "Iron Plate"
```

---

# Summary of Validation Requirements

| Rule                        | Applies To   | Requirement                                   |
|-----------------------------|--------------|-----------------------------------------------|
| Non-empty `name`            | Item, Recipe | Must contain non-whitespace text              |
| Non-empty `inputMaterials`  | Recipe       | Must contain at least one entry               |
| Non-empty `outputMaterials` | Recipe       | Must contain at least one entry               |
| Positive material amounts   | Recipe       | All quantities must be `> 0`                  |
| Valid `primaryOutput`       | Recipe       | Must exactly match a key in `outputMaterials` |
