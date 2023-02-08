/**
 * This program is given devices coordinates (x, y) and a list of network stations with their coordinates and reach.
 * It then finds the network stations within reach for specific coordinates, and selects the network station with the
 * highest non-zero speed (if any). Speed is inversely proportionate to distance and is defined in detail in get_speed().
 *
 * Author: Antti Yli-Tainio
 *
 * Email: firstname.lastname(ät)iki.fi
 * 
 * Copyright: Antti Yli-Tainio, 2022. Free to use for review purposes.
 */


/** Returns the euclidean distance between two points by comparing their properties x and y.
 *  - @param pointA: Point A in 2D space (x, y).
 *  - @param pointB: Point B in 2D space (x, y).
 */
function get_distance_between(pointA, pointB) {
    return Math.sqrt(Math.pow((pointA.x - pointB.x), 2) + Math.pow((pointA.y - pointB.y), 2));
}


/** Returns the speed of a connection, as desribed in given specification.
 *  - @param network_station_reach: Reach of candidate network station.
 *  - @param distance: Euclidean distance between network station and device coordinates.
 */
function get_speed(network_station_reach, distance) {
    return Math.pow((network_station_reach - distance), 2);
}


/** Finds network station with best speed for given device coordinates. Returns station coordinates and connection speed.
 *  - @param device: Coordinates for device looking for a network station.
 *  - @param network_stations: Network stations with coordinates and reach from each.
 */
function find_fastest_network_station(device, network_stations) {
    var candidate_station = { x: NaN, y: NaN, speed: 0 } 

    // Check all network stations for this particular device.
    for (station of network_stations) {
        var distance = get_distance_between(device, station);
  
        // Make sure the station reach is able to cover the distance. If equal, speed becomes zero so this case is not included.
        if (station.reach > distance) {
            var current_station_speed = get_speed(station.reach, distance);
            
            // Select the network station if it a has higher (non-zero) speed than the current best candidate.
            // NOTE: Selection criteria not specified for equal speed. Keeping current candidate if speed is equal. A speed <= 0 is rejected.
            if (current_station_speed > candidate_station.speed && current_station_speed > 0) {
                candidate_station.x = station.x, 
                candidate_station.y = station.y;
                candidate_station.speed = current_station_speed;
            }
        }
    }

    // Function returns fastest network station coordinates and achieved connection speed. 
    // Or if valid station not found, returns NaN for coordinates and 0 for speed.
    return candidate_station;
}


/** Prints out information about the fastest found network station, or error information if valid station not found.
 *  - @param device: Coordinates for device looking for a network station.
 *  - @param fastest_station: Best network station (x, y) coordinates and resulting connection speed. Speed == 0 and coordinates == NaN if none.
 */
function print_out_fastest_station(device, fastest_station) {
    document.write("<div>");
    document.write("<h3>Device at coordinates (" + device.x + ", " + device.y + "):</h3>");
    if (fastest_station.speed === 0) {
        document.write("<ul><li>No network station within reach.</li></ul>");
    }
    else {
        document.write("<ul><li>Best network station for this device is located at (" + fastest_station.x + "," + fastest_station.y + ") with speed " + fastest_station.speed + ".</li></ul>");
    }
    document.write("</div>");
}


/************************ UNIT TESTS ************************/

// Unit test for get_distance_between().
function unit_test_get_distance_between() {
    // Same point. Distance == 0.
    point_a = { x: 1, y: 1 };
    point_b = { x: 1, y: 1 };
    console.assert(get_distance_between(point_a, point_b) === 0);

    // One coordinate off by 1 unit. Distance == 1.
    point_a = { x: 1, y: 1 };
    point_b = { x: 0, y: 1 }; 
    console.assert(get_distance_between(point_a, point_b) === 1);

    // Both coordinates off by 1. Distance should be sqrt(2);
    point_a = { x: 0, y: 0 };
    point_b = { x: 1, y: 1 }; 
    console.assert(get_distance_between(point_a, point_b) == Math.sqrt(2));

    // One coordinate is invalid. Distance == NaN.
    point_a = { x: "hello", y: "world" };
    point_b = { x: 0, y: 1 }; 
    console.assert(isNaN(get_distance_between(point_a, point_b)));

    return 0;
}


// Unit test for function get_speed().
function unit_test_get_speed() {
    // Function should return NaN if either parameter is NaN.
    console.assert(isNaN(get_speed(NaN, 1)));
    console.assert(isNaN(get_speed(1, NaN)));
    console.assert(isNaN(get_speed(NaN, NaN)));

    // With the first paremeter always +1 larger than the second, should always return 1^2 == 1.
    for (var i = 0; i < 50; ++i) {
        console.assert(get_speed((i+1), i) === 1);
    }

    return 0;
}


/* Test case for find_fastest_network_station() where coordinates are identical to network station coordinates.
 * With identical reach on all network stations, this always identify the network station with the same coordinates.
 */
function unit_test_find_fastest_network_station_identical_coordinates() {
    var network_stations_as_same_coordinates = [
        { x: -1,   y: -1,   reach: 10 },
        { x: 0,    y: 0,    reach: 10 },
        { x: 1,    y: 1,    reach: 10 }, 
        { x: 2,    y: 2,    reach: 10 },
        { x: 3,    y: 3,    reach: 10 },
        { x: 4,    y: 4,    reach: 10 },
        { x: 5.5,  y: 5.5,  reach: 10 },
        { x: 100,  y: 100,  reach: 10 }
    ];

    for (i of network_stations_as_same_coordinates) {
        var fastest_station = find_fastest_network_station(i, network_stations_as_same_coordinates, false);
        // With identical coordinates, returned speed should always be (reach)^2.
        console.assert(fastest_station.speed === Math.pow((i.reach), 2));
    }

    return 0;
}


// Unit tests find_fastest_network_station() with invalid inputs.
function unit_test_find_fastest_network_station_invalid_parameters() {
    
    // Test case A: At least one device coordinate is invalid.

    var all_network_stations = [
        { x: 0,  y: 0,  reach: 10000 },
        { x: 100, y: 100, reach: 10000 }, 
    ];

    // Coordinates for devices trying to connect to a network station.
    var all_devices_to_test = [ 
        { x: NaN, y: 0   }, 
        { x: NaN, y: 100 }, 
        { x: NaN, y: 10  }, 
        { x: 18,  y: NaN }, 
        { x: 13,  y: NaN }, 
        { x: NaN, y: NaN }
    ];

    // Despite the more than enough range (10000), should always return an invalid station, as at least one coordinate is NaN.
    for (device of all_devices_to_test) {
        fastest_station = find_fastest_network_station(device, all_network_stations, false);
        console.assert(fastest_station.speed === 0);
        console.assert(isNaN(fastest_station.x));
        console.assert(isNaN(fastest_station.y));
    }


    // Test case B: At least one coordinate or reach is invalid for all stations. Should always return an invalid station.
    
    all_network_stations = [
        { x: NaN, y: 0,   reach: 10000 },
        { x: 0,   y: NaN, reach: 10000 },
        { x: 0,   y: 0,   reach: NaN   }
    ];

    single_device = { x: 0, y: 0 };

    fastest_station = find_fastest_network_station(single_device, all_network_stations, false);
    console.assert(fastest_station.speed === 0);
    console.assert(isNaN(fastest_station.x));
    console.assert(isNaN(fastest_station.y));
}


// Tests valid and invalid test cases for randomly placed devices / network stations.
function unit_test_find_fastest_network_station_random() {
    // Test case A: Valid random test cases: network station just within reach.
    for (var i = 0; i < 100; ++i) {
        // Random device coordinates between -1000 and 1000.
        var x_coordinate = Math.floor(Math.random() * 2000) - 1000;
        var y_coordinate = Math.floor(Math.random() * 2000) - 1000;
        device = { x: x_coordinate, y: y_coordinate };

        // Place the a network device within a distance of < 5 from the device (station reach == 5).
        var network_stations = [ 
            { x: (device.x - 3),  y: (device.y - 3),  reach: 5 }, // Distance == ~ 4.2. Within reach 5.
            { x: 10000, y: -10000, reach: 0 }                     // This station is always out of reach.
        ];

        fastest_station = find_fastest_network_station(device, network_stations);
        console.assert(fastest_station.speed != 0);
        console.assert(fastest_station.x === network_stations[0].x);
        console.assert(fastest_station.y === network_stations[0].y);
    }

    // Test Case B: Invalid random test cases: network station just out of reach.
    for (var i = 0; i < 100; ++i) {
        // Random device coordinates between -1000 and 1000.
        var x_coordinate = Math.floor(Math.random() * 2000) - 1000;
        var y_coordinate = Math.floor(Math.random() * 2000) - 1000;
        device = { x: x_coordinate, y: y_coordinate };

        // Place the a network station just out of reach, so no station is detected.
        var network_stations = [ 
            { x: (device.x - 3),  y: (device.y - 4),  reach: 5 }, // Distance == 5. Technically within reach but treated as just out of reach due to 0 speed.
            { x: 10000, y: -10000, reach: 0 }                     // This station is always out of reach.
        ];

        fastest_station = find_fastest_network_station(device, network_stations);
        console.assert(fastest_station.speed === 0);
        console.assert(isNaN(fastest_station.x));
        console.assert(isNaN(fastest_station.y));
    }
}


// Runs all unit tests for this program.
function run_unit_tests() {
    unit_test_get_distance_between();
    unit_test_get_speed();
    unit_test_find_fastest_network_station_identical_coordinates();
    unit_test_find_fastest_network_station_invalid_parameters();
    unit_test_find_fastest_network_station_random();
}

/************************ END OF: UNIT TESTS ************************/


// Main program code is here and is run right below definition.
function main() {
    // Coordinates and reach for network devices that other devices can connect to.
    var all_network_stations = [
        { x: 0,  y: 0,  reach: 9  },
        { x: 20, y: 20, reach: 6  }, 
        { x: 10, y: 0,  reach: 12 },
        { x: 5,  y: 5,  reach: 13 },
        { x: 99, y: 25, reach: 2  }
    ];

    // Coordinates for devices trying to connect to a network station.
    var all_devices_to_test = [ 
        { x: 0,   y: 0   }, 
        { x: 100, y: 100 }, 
        { x: 15,  y: 10  }, 
        { x: 18,  y: 18  }, 
        { x: 13,  y: 13  }, 
        { x: 25,  y: 99  }
    ];

    // Go through all devices of interest and try to find a network station with best possible speed. Print out results.
    for (device of all_devices_to_test) {
        fastest_station = find_fastest_network_station(device, all_network_stations);
        print_out_fastest_station(device, fastest_station);
    }

    run_unit_tests();
}

main();
