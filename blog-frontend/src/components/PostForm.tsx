
import { useEffect, useState } from "react";
import {EditorContent, useEditor} from '@tiptap/react';
import StartertKit from '@tiptap/starter-kit'
import Heading, { Level } from "@tiptap/extension-heading";
import BulletList from '@tiptap/extension-bullet-list';
import ListItem from '@tiptap/extension-list-item'
import { Button, Card, CardBody, Chip, Dropdown, DropdownItem, DropdownMenu, DropdownTrigger, Input, Select, SelectItem, SelectSection } from "@nextui-org/react";
import { Bold, ChevronDown, Italic, List, ListOrdered, Redo, Undo, X } from "lucide-react";

import { Category, Post, PostStatus, Tag } from "@/services/apiService";

interface PostFormProps {
    initialPost?: Post | null;
    onSubmit: (postData: {
        title: string;
        content: string;
        categoryId: string;
        tagids: string[];
        status: PostStatus;
    }) => Promise<void>;
    onCancel: () => void;
    categories: Category[];
    availableTags: Tag[];
    isSubmitting?: boolean;
}

const PostForm: React.FC<PostFormProps> = ({
    initialPost,
    onSubmit,
    onCancel,
    categories,
    availableTags,
    isSubmitting = false,
}) => {
    const [title, setTitle] = useState(initialPost?.title || '');
    const [categoryId, setCategoryId] = useState(initialPost?.category?.uuid || '');
    const [selectedTags, setSelectedTags] = useState<Tag[]>(initialPost?.tags || []);
    const [status, setStatus] = useState<PostStatus>(
        initialPost?.status || PostStatus.DRAFT
    );
    
    const [errors, setErrors] = useState<Record<string, string>>({});

    const editor = useEditor({
        extensions: [
            StartertKit.configure({
                heading: false,
                bulletList: false,
                orderedList: false,
            }),
            Heading.configure({
                levels: [1, 2, 3],
            }),
            BulletList.configure({
                keepMarks: true,
                keepAttributes: false,
            }),
            ListItem,         
        ],
        content: initialPost?.content || '',
        editorProps: {
            attributes: {
                class: 'prose max-w-none focus:outline-none min-h-[400px] px-4 py-2 border rounded-lg'
            },
        },
    });

    useEffect(() => {
        if (initialPost && editor ) {
            setTitle(initialPost.title);
            editor.commands.setContent(initialPost.content);
            setCategoryId(initialPost.category.uuid)
            setSelectedTags(initialPost.tags)
            setStatus(initialPost.status || PostStatus.DRAFT);
        }
    }, [initialPost, editor]);


    const validateForm = (): boolean => {
        const newErrors: Record<string, string> = {};

        if (!title.trim()) {
            newErrors.title = "Title is required";
        }
        if (!editor?.getHTML() || editor?.getHTML() === '<p></p>') {
            newErrors.content = "Content is required";
        }
        if (!categoryId) {
            newErrors.category = "Category is required";
        }

        setErrors(newErrors);

        return Object.keys(newErrors).length === 0;
    }

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();

        if (!validateForm()) {
            return;
        }

        await onSubmit({
            title: title.trim(),
            content: editor?.getHTML() || '',
            categoryId: categoryId,
            tagids: selectedTags.map(tag => tag.uuid),
            status,
        });
    };

    const handleAddTag = (tag: Tag) => {
        if (tag && !selectedTags.includes(tag) && selectedTags.length < 10) {
            setSelectedTags([...selectedTags, tag]);
        }
    };

    const handleRemoveTag = (tagToRemove: Tag) => {
        setSelectedTags(selectedTags.filter(tag => tag !== tagToRemove));
    };

    const handleHeadingSelect = (level: Level) => {
        editor?.chain().focus().toggleHeading({ level }).run();
    };

    const sugesstedTags = availableTags
    .filter(tag => !selectedTags.includes(tag))
    .slice(0, 5);

    return (
        <form className="space-y-6" onSubmit={handleSubmit}>
            <Card>
                <CardBody className="space-y-4">
                    <div className="space-y-2">
                        <Input isRequired
                        errorMessage={errors.title}
                        isInvalid={!!errors.title}
                        type="title"
                        value={title}
                        onChange={(e)=> setTitle(e.target.value)}
                        />
                    </div>

                    <div className="space-y-2">
                        <div className="bg-default-100 p-2 rounded-lg mb-2 flex gap-2 flex-wrap items-center">
                            <Dropdown>
                                <DropdownTrigger>
                                    <Button
                                    endContent={<ChevronDown size={16}/>}
                                    size="sm"
                                    variant="flat"
                                    >
                                    Heading
                                    </Button>
                                </DropdownTrigger>
                                <DropdownMenu aria-label="Heading levels"
                                onAction={(key)=>{
                                    const lvl = Number(key)

                                    if (![1,2,3].includes(lvl)) return;
                                    handleHeadingSelect(lvl as Level);
                                } 
                                }
                                    >
                                        <DropdownItem key="1" className={editor?.isActive('heading', { level: 1 }) ? 'bg-default-200' : ''}>
                                            Heading 1
                                        </DropdownItem>

                                        <DropdownItem key="2" className={editor?.isActive('heading', {level: 2}) ? 'bg-default-200' : ''}>
                                            Heading 2
                                        </DropdownItem>

                                        <DropdownItem key="3" className={editor?.isActive('heading', { level: 3 })? 'bg-default-200' : ''}>
                                            Heading 3
                                        </DropdownItem>
                                </DropdownMenu>
                            </Dropdown>

                            <Button
                            isIconOnly
                            className={editor?.isActive('bold') ? 'bg-default-200' : ''}
                            size="sm"
                            variant="flat"
                            onClick={()=> editor?.chain().focus().toggleBold().run()}
                            >
                                <Bold size={16}/>
                            </Button>
                            <Button
                            isIconOnly
                            className={editor?.isActive('italic') ? 'bg-default-200' : ''}
                            size="sm"
                            variant="flat"
                            onClick={() => editor?.chain().focus().toggleItalic().run()}
                             >
                             <Italic size={16} />
                            </Button>

                            <div className="h-6 w-px bg-default-300 mx-2"/>
                            
                            <Button
                            isIconOnly
                            className={editor?.isActive('list') ? 'bg-default-200' : ''}
                            size="sm"
                            variant="flat"
                            onClick={() => editor?.chain().focus().toggleBulletList().run()}
                            >
                             <List size={16} />
                            </Button>
                            <Button
                            isIconOnly
                            className={editor?.isActive('orderedList') ? 'bg-default-200' : ''}
                            size="sm"
                            variant="flat"
                            onClick={() => editor?.chain().focus().toggleBulletList().run()}
                            >
                             <ListOrdered size={16} />
                            </Button>
                            

                            <div className="h-6 w-px bg-default-300 mx-2"/>

                            <Button
                            isIconOnly
                            className={editor?.isActive('list') ? 'bg-default-200' : ''}
                            isDisabled={!editor?.can().undo()}
                            size="sm"
                            variant="flat"
                            onClick={() => editor?.chain().focus().undo().run() }
                            >
                                <Undo size={16} />
                            </Button>
                            <Button
                            isIconOnly
                            className={editor?.isActive('list') ? 'bg-default-200' : ''}
                            isDisabled={!editor?.can().redo()}
                            size="sm"
                            variant="flat"
                            onClick={() => editor?.chain().focus().redo().run() }
                            >
                                <Redo size={16} />
                            </Button>
                        </div>
                        <EditorContent editor={editor} />
                        {errors.content && (
                            <div className="text-danger text-sm">{errors.content}</div>
                        )}
                    </div>
                    
                    <div className="space-y-2">
                        <Select 
                        isRequired
                        errorMessage={errors.category}
                        isInvalid={!!errors.category}
                        label='Category'
                        selectedKeys={categoryId ? [categoryId] : []}
                        onChange={(e)=> setCategoryId(e.target.value)}
                        >
                         {categories.map((cat) => (
                            <SelectItem key={cat.uuid} value={cat.uuid}>
                                {cat.name}
                            </SelectItem>
                         ))}
                        </Select>
                    </div>

                    <div className="space-y-2">
                         <Select
                         label="Add Tags"
                         selectedKeys={selectedTags.map(tag => tag.uuid)}
                         >
                            <SelectSection>
                                {sugesstedTags.map((tag)=> (
                                    <SelectItem
                                    key={tag.uuid}
                                    value={tag.uuid}
                                    onClick={() => handleAddTag}
                                    >
                                        {tag.name}
                                    </SelectItem>
                                ))}
                            </SelectSection>
                         </Select>

                         <div className="flex flex-wrap gap-2 mt-2">
                            {selectedTags.map((tag)=> (
                                <Chip
                                key={tag.uuid}
                                endContent={<X size={14}/>}
                                variant="flat"
                                onClose={()=> handleRemoveTag(tag)}
                                >
                                 {tag.name}
                                </Chip>
                            ))}
                         </div>
                    </div>

                    <div>
                        <Select
                        label="Status"
                        selectedKeys={[status]}
                        onChange={(e) => setStatus(e.target.value as PostStatus)}
                        >
                            <SelectItem
                            key={PostStatus.DRAFT}
                            value={PostStatus.DRAFT}
                            >Draft
                            </SelectItem>
                            <SelectItem
                            key={PostStatus.PUBLISHED}
                            value={PostStatus.PUBLISHED}
                            >Published
                            </SelectItem>
                        </Select>
                    </div>
                    
                    <div className="flex justify-end gap-2 pt-2">
                        <Button
                        color="danger"
                        disabled={isSubmitting}
                        variant="flat"
                        onClick={onCancel}
                        >
                          cancel
                        </Button>
                        <Button
                        color="primary"
                        isLoading={isSubmitting}
                        type="submit"
                        >
                            {initialPost ? 'Update' : 'Create'} Post
                        </Button>
                    </div>
                </CardBody>
            </Card>
        </form>
    );
};

export default PostForm;