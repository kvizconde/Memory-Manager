package memory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * MemoryManager.
 * 
 * A simple memory management application that takes in a file as the input
 * (command line argument) to manage memory using three different algorithms.
 * 
 * @author Kevin Vizconde
 */
public class MemoryManager
{

    /**
     * The Class MemoryBlock.
     */
    public class MemoryBlock
    {
        /** The pid. */
        private String pid;

        /** The size. */
        private int size;

        /** The start. */
        private int start;

        /**
         * Instantiates a new memory block.
         *
         * @param pid
         *            the pid
         * @param size
         *            the size
         * @param start
         *            the start
         */
        public MemoryBlock(String pid, int size, int start)
        {
            this.size = size;
            this.start = start;
            this.pid = pid;
        }

        /**
         * Checks if is in use.
         *
         * @return true, if is in use
         */
        public boolean isInUse()
        {
            return (pid != null);
        }

        /**
         * Sets the size.
         *
         * @param size
         *            the new size
         */
        public void setSize(int size)
        {
            this.size = size;
        }

        /**
         * Sets the start.
         *
         * @param start
         *            the new start
         */
        public void setStart(int start)
        {
            this.start = start;
        }

        /**
         * Gets the pid.
         *
         * @return the pid
         */
        public String getPid()
        {
            return pid;
        }

        /**
         * Gets the size.
         *
         * @return the size
         */
        public int getSize()
        {
            return size;
        }

        /**
         * Gets the start.
         *
         * @return the start
         */
        public int getStart()
        {
            return start;
        }

        /**
         * Dealloc.
         * 
         * This method deallocates the memory for process with id PID;
         */
        public void dealloc()
        {
            pid = null;
        }

        public String toString()
        {
            String processSize = " | process size : " + size;
            return start + "\tto\t" + (start + size - 1) + " is "
                    + ((pid != null) ? "used by pid " + pid + processSize : "empty");
        }
    }

    /**
     * Find index by method.
     *
     * @param memory
     *            the memory
     * @param new_block_size
     *            the new block size
     * @param method
     *            the method
     * @return if we find it fits we return non-negative value, otherwise we return
     *         -1
     */
    public static int findIndexByMethod(List<MemoryBlock> memory, int new_block_size, int method)
    {
        int index = -1;

        // First Fit Algorithm
        if (method == 1)
        {
            // loop through the current memory block
            for (MemoryBlock cur : memory)
            {
                index++;
                // check if current memory is free and the size of memory is greater or equal
                // to the new block
                if (!cur.isInUse() && cur.getSize() >= new_block_size)
                {
                    return index;
                }
            }
            return -1;
        }

        // Best Fit Algorithm
        else if (method == 2)
        {
            int prevSize = Integer.MAX_VALUE;
            int tempIndex = 0;

            // loop through the current memory block
            for (MemoryBlock cur : memory)
            {
                // check if current memory is free and the size is greater or equal to the new
                // block and the current memory size is less than the previous memory size
                if (!cur.isInUse() && cur.getSize() >= new_block_size && cur.getSize() < prevSize)
                {
                    index = tempIndex; // index stores the best size
                    prevSize = cur.getSize(); // assign current memory size to the previous memory size
                }
                tempIndex++;
            }
        }

        // Worst Fit Algorithm
        else if (method == 3)
        {
            int preSize = 0;
            int tempIndex = 0;
            for (MemoryBlock cur : memory)
            {
                // check if current memory is free and the size is greater or equal to the new
                // block and the current memory size is greater than the previous memory size
                if (!cur.isInUse() && cur.getSize() >= new_block_size && cur.getSize() > preSize)
                {
                    index = tempIndex;
                    preSize = cur.getSize();
                }
                tempIndex++;
            }
        }
        return index;
    }

    /**
     * Gets the index by pid.
     *
     * @param memory
     *            the memory
     * @param pid
     *            the pid
     * @return the index by pid
     */
    public static int getIndexByPid(List<MemoryBlock> memory, String pid)
    {
        for (int i = 0; i < memory.size(); i++)
        {
            // check if memory block is in use and PID in that block is equal to the PID we
            // are looking for
            if (memory.get(i).isInUse() && memory.get(i).getPid().equals(pid))
            {
                return i;
            }
        }
        return -1;
    }

    /**
     * Memory Manager.
     * 
     * This method takes in a file and handles how memory will be allocated based on
     * the memory allocation algorithm that the user chooses.
     * 
     * 1 : First Fit || 2 : Best Fit || Fit 3 : Worst Fit
     * 
     * @param filename
     */
    public static void memoryManager(String filename)
    {
        File fileIn = new File(filename);
        Scanner sc;
        try
        {
            sc = new Scanner(fileIn);
            int method = sc.nextInt();
            int memory_size = sc.nextInt();
            sc.nextLine();
            List<MemoryBlock> memory = new ArrayList<>();

            MemoryManager mem = new MemoryManager();

            // init of memory block
            memory.add(mem.new MemoryBlock(null, memory_size, 0));

            while (sc.hasNext())
            {
                String option = sc.next();

                // allocate memory
                if (option.charAt(0) == 'A')
                {
                    String pid = sc.next();
                    int process_memory_size = sc.nextInt();

                    // set the index based on the algorithm selected
                    int index = findIndexByMethod(memory, process_memory_size, method);

                    if (index < 0)
                    {
                        List<MemoryBlock> tempMemory = new ArrayList<>();
                        int tempStart = 0;
                        for (MemoryBlock cur : memory)
                        {
                            if (cur.isInUse())
                            {
                                cur.setStart(tempStart);
                                tempMemory.add(cur); // update the current block and add to the new list
                                tempStart += cur.getSize(); // keeps track of where the next block starts with
                            }
                        }
                        if (tempStart < memory_size)
                        {
                            tempMemory.add(mem.new MemoryBlock(null, memory_size - tempStart, tempStart));
                        }
                        memory = tempMemory;
                        index = findIndexByMethod(memory, process_memory_size, method);
                    }
                    // ensures index always starts at 0
                    if (index > -1)
                    {
                        memory.add(index, mem.new MemoryBlock(pid, process_memory_size, memory.get(index).start));
                        MemoryBlock x = memory.get(index + 1);
                        if (!x.isInUse())
                        {
                            x.setSize(x.getSize() - process_memory_size);
                            x.setStart(x.getStart() + process_memory_size);

                            // handles the case if the gap between blocks is the same size of what we need,
                            // after that we don't have a gap, because what we get matches up exactly
                            // with that block size
                            if (x.getSize() == 0)
                            {
                                memory.remove(index + 1);
                            }
                        }

                    }
                }
                // print the blocks of memory
                else if (option.charAt(0) == 'P')
                {
                    for (MemoryBlock current_block : memory)
                    {
                        System.out.println(current_block);
                    }
                }
                // deallocate memory
                else if (option.charAt(0) == 'D')
                {
                    String pid = sc.next();
                    int index = getIndexByPid(memory, pid);

                    MemoryBlock current = memory.get(index);
                    current.dealloc(); // remove PID

                    // checks the next block, if the next block is not in use, we merge them
                    if (index < memory.size() - 1 && !memory.get(index + 1).isInUse())
                    {
                        current.setSize(current.getSize() + memory.get(index + 1).getSize());
                        memory.remove(index + 1);
                    }
                    // checks the previous block, if the previous block is not in use, we merge them
                    if (index != 0 && !memory.get(index - 1).isInUse())
                    {
                        memory.get(index - 1).setSize(current.getSize() + memory.get(index - 1).getSize());
                        memory.remove(index);
                    }
                }

            }

            sc.close();
        }
        catch (Exception e)
        {
            System.out.println("File Not Found" + e.getMessage());

        }

    }

    /**
     * The main method.
     *
     * @param args
     *            the arguments
     */
    public static void main(String[] args)
    {
        System.out.println("Reading From File : " + args[0] + "\n");
        memoryManager(args[0]);
    }

}
