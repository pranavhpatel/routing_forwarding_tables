import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * 
 * @author Pranav Patel Class that contains the code for A3 LinkState class will
 *         implement Dijkstras algorithm
 *
 */
public class LinkState {
	public static int total_routers;
	public static int number_of_gateways;
	public static int[] gateway_routers;
	public static int[][] cost_matrix;
	public static int[] routers;
	public static int MAX = Integer.MAX_VALUE;
	public static String[][] table;

	/**
	 * 
	 * @param args main function, all input processing is done here other functions
	 *             for assignment are called
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		String text[];
		String file_path = "";
		for (int i = 0; i < args.length; i++) {
			file_path = file_path + args[i] + " ";
		}
		read_file(file_path);
		for (int i = 0; i < total_routers; i++) {
			dijkstra(i);
		}
	}

	/**
	 * 
	 * @param infile
	 * @throws IOException function to read file line by line
	 */
	public static void read_file(String infile) throws IOException {
		String in_line;
		String[] break_line;
		routers = new int[total_routers];

		FileReader fr = new FileReader(infile);
		BufferedReader reader = new BufferedReader(fr);
		total_routers = Integer.parseInt(reader.readLine().trim());

		cost_matrix = new int[total_routers][total_routers];
		for (int i = 0; i < total_routers; i++) {
			in_line = reader.readLine().trim();
			break_line = in_line.split(" ");
			for (int x = 0; x < total_routers; x++) {
				cost_matrix[i][x] = Integer.parseInt(break_line[x]);
			}
		}
		break_line = reader.readLine().trim().split(" ");
		number_of_gateways = break_line.length;
		gateway_routers = new int[number_of_gateways];
		for (int y = 0; y < number_of_gateways; y++) {
			gateway_routers[y] = Integer.parseInt(break_line[y]);
		}
	}

	/**
	 * 
	 * @param source main algorithm for computing a3
	 * @throws UnsupportedEncodingException 
	 * @throws FileNotFoundException 
	 */
	public static void dijkstra(int source) {
		int distances[] = new int[total_routers];
		Boolean visited[] = new Boolean[total_routers];
		int path[] = new int[total_routers];
		table = new String[total_routers][total_routers];

		for (int i = 0; i < total_routers; i++) {
			distances[i] = MAX;
			visited[i] = false;
		}
		distances[source] = 0;
		path[source] = -1; // no previous node
		for (int p = 0; p < total_routers; p++) {
			int min = MAX;
			int index = -1;
			for (int i = 0; i < total_routers; i++) {
				if (visited[i] == false && distances[i] <= min) {
					min = distances[i];
					index = i;
				}
			}
			visited[index] = true;
			for (int x = 0; x < total_routers; x++) {
				int weight = cost_matrix[index][x];
				if (!visited[x] && weight != 0 && distances[index] != MAX && distances[index] + weight < distances[x]
						&& weight != -1) {
					distances[x] = distances[index] + weight;
					table[p][x] = distances[x] + "," + index;
				}
			}
		}
//		for (int r = 0; r < total_routers; r++) {
//			for (int f = 0; f < total_routers; f++) {
//				System.out.print(" " + table[r][f]);
//			}
//			System.out.println(" ");
//		}
		print_distances(distances, source);
	}

	/**
	 * 
	 * @param distances
	 * @param visited
	 * @return index gets index of min for algorithm
	 */
	public static int min_distance(int distances[], Boolean visited[]) {
		int min = MAX;
		int index = -1;
		for (int i = 0; i < total_routers; i++) {
			if (visited[i] == false && distances[i] <= min) {
				min = distances[i];
				index = i;
			}
		}
		return index;
	}

	/**
	 * 
	 * @param distances output function main output function for a3
	 * @throws UnsupportedEncodingException 
	 * @throws FileNotFoundException 
	 */
	public static void print_distances(int distances[], int source) {
		String results = "";
		boolean is_gateway = false;
		for (int x = 0; x < number_of_gateways; x++) {
			if (gateway_routers[x] == source + 1) {
				is_gateway = true;
			}
		}

		if (is_gateway == false) {

			for (int y = 0; y < total_routers; y++) {// -1 for no path according to a3 consistency
				if (distances[y] == MAX) {
					distances[y] = -1;
				}
			}
			System.out.println("Forwarding Table for " + (source + 1));
			System.out.println("\tTo\tCost\tNext Hop");

			for (int i = 0; i < number_of_gateways; i++) {
				System.out.println("\t" + gateway_routers[i] + "\t" + distances[gateway_routers[i] - 1] + "\t"
						+ find_next_node(source));
			}
			
		}
	}

	/**
	 * 
	 * @param source
	 * @return
	 * function to find the next hop from source
	 */
	public static int find_next_node(int source) {
		int row;
		int column;
		int next = -1;
		int previous = -1;
		int current = -1;
		String pair = null;
		String split[];
		boolean run = true;
		boolean down_row = false;
		row = total_routers-1;
		column = total_routers-1;
		//System.out.println(row + " " + column);
		while (run) {
			down_row = false;
			if (column == -1) {
				column = total_routers-1;
				down_row = true;
				row--;
			}
			if (row <= -1) {
				run = false;
				break;
			}
			pair = table[row][column] + " ";
			if (!pair.equals("null ")) {
				split = pair.split(",");
				previous = Integer.parseInt(split[1].trim());
				if (previous != source) {
					column = previous;
				}
				else {
					return column+1;
				}
				row--;
			} else {
				column--;
			}

		}

		return next;
	}

}
