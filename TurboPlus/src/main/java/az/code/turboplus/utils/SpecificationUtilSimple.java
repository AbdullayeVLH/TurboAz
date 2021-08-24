package az.code.turboplus.utils;

import az.code.turboplus.enums.BodyType;
import az.code.turboplus.enums.Color;
import az.code.turboplus.enums.FuelType;
import az.code.turboplus.enums.GearBox;
import az.code.turboplus.models.Listing;
import az.code.turboplus.models.Subscription;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;

public class SpecificationUtilSimple {

    public static Specification<Listing> sameBodyType(String bodyType) {
        return (root, query, criteriaBuilder) -> {
            if (bodyType != null)
                return criteriaBuilder.equal(root.get("bodyType"), BodyType.valueOf(bodyType));
            return criteriaBuilder.conjunction();
        };
    }

    public static Specification<Listing> sameFuelType(String fuelType) {
        return (root, query, criteriaBuilder) -> {
            if (fuelType != null)
                return criteriaBuilder.equal(root.get("fuelType"), FuelType.valueOf(fuelType));
            return criteriaBuilder.conjunction();
        };
    }

    public static Specification<Listing> sameString(String name, String paramName) {
        return (root, query, criteriaBuilder) -> {
            if (name != null)
                return criteriaBuilder.equal(root.get("baseListing").get(paramName).get("name"), name);
            return criteriaBuilder.conjunction();
        };
    }

    public static Specification<Listing> hasBooleanOption(Boolean option, String paramName) {
        return (root, query, criteriaBuilder) -> {
            if (option != null)
                return criteriaBuilder.equal(root.get("baseListing").get(paramName), option);
            return criteriaBuilder.conjunction();
        };
    }

    public static Specification<Listing> hasRange(Integer minValue, Integer maxValue, String paramName) {
        return (root, query, criteriaBuilder) -> {
            if (minValue == null && maxValue == null)
                return criteriaBuilder.conjunction();
            return criteriaBuilder.between(root.get("baseListing").get(paramName), minValue != null ? minValue : 0,
                    maxValue != null ? maxValue : Integer.MAX_VALUE);
        };
    }

    public static Specification<Subscription> integerBetween(Integer value, String paramName) {
        return (root, query, criteriaBuilder) -> {
            if (value != null) {
                Predicate greater = criteriaBuilder.lessThanOrEqualTo(root.get("min" + capitalize(paramName)), value);
                Predicate isMinNull = criteriaBuilder.isNull(root.get("min" + capitalize(paramName)));
                Predicate lesser = criteriaBuilder.greaterThanOrEqualTo(root.get("max" + capitalize(paramName)), value);
                Predicate isMaxNull = criteriaBuilder.isNull(root.get("max" + capitalize(paramName)));
                return criteriaBuilder.and(criteriaBuilder.or(greater, isMinNull), criteriaBuilder.or(lesser, isMaxNull));
            }
            return criteriaBuilder.conjunction();
        };
    }

    public static Specification<Subscription> sameBodyTypeForSubs(String bodyType) {
        return (root, query, criteriaBuilder) -> {
            if (bodyType != null) {
                Predicate bodyTypeNull = criteriaBuilder.isNull(root.get("bodyType"));
                Predicate bodyTypeNotNull = criteriaBuilder.equal(root.get("bodyType"), BodyType.valueOf(bodyType));
                return criteriaBuilder.or(bodyTypeNotNull, bodyTypeNull);
            }
            return criteriaBuilder.conjunction();
        };
    }

    public static Specification<Subscription> sameColorForSubs(String color) {
        return (root, query, criteriaBuilder) -> {
            if (color != null) {
                Predicate colorNull = criteriaBuilder.isNull(root.get("color"));
                Predicate colorNotNull = criteriaBuilder.equal(root.get("color"), Color.valueOf(color));
                return criteriaBuilder.or(colorNull, colorNotNull);
            }
            return criteriaBuilder.conjunction();
        };
    }

    public static Specification<Subscription> sameGearBoxForSubs(String gearBox) {
        return (root, query, criteriaBuilder) -> {
            if (gearBox != null) {
                Predicate gearBoxNull = criteriaBuilder.isNull(root.get("gearBox"));
                Predicate gearBoxNotNull = criteriaBuilder.equal(root.get("gearBox"), GearBox.valueOf(gearBox));
                return criteriaBuilder.or(gearBoxNotNull, gearBoxNull);
            }
            return criteriaBuilder.conjunction();
        };
    }

    public static Specification<Subscription> sameFuelTypeForSubs(String fuelType) {
        return (root, query, criteriaBuilder) -> {
            if (fuelType != null) {
                Predicate fuelTypeNull = criteriaBuilder.isNull(root.get("fuelType"));
                Predicate fuelTypeNotNull = criteriaBuilder.equal(root.get("fuelType"), FuelType.valueOf(fuelType));
                return criteriaBuilder.or(fuelTypeNull, fuelTypeNotNull);
            }
            return criteriaBuilder.conjunction();
        };
    }

    public static Specification<Subscription> hasBooleanOptionForSubs(Boolean option, String paramName) {
        return (root, query, criteriaBuilder) -> {
            if (option != null) {
                Predicate typeNull = criteriaBuilder.isNull(root.get(paramName));
                Predicate typeNotNull = criteriaBuilder.equal(root.get(paramName), option);
                return criteriaBuilder.or(typeNull, typeNotNull);
            }
            return criteriaBuilder.conjunction();
        };
    }

    public static Specification<Subscription> sameStringForSubs(String name, String paramName) {
        return (root, query, criteriaBuilder) -> {
            if (name != null) {
                Predicate typeNull = criteriaBuilder.isNull(root.get(paramName).get("name"));
                Predicate typeNotNull = criteriaBuilder.equal(root.get(paramName).get("name"), name);
                return criteriaBuilder.or(typeNull, typeNotNull);
            }
            return criteriaBuilder.conjunction();
        };
    }

    private static String capitalize(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
